package eu.giof.poc.rest.impl.payment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import eu.giof.poc.rest.PaymentService;
import eu.giof.poc.rest.body.PaymentBody;
import eu.giof.poc.rest.dto.PaymentResultDto;
import eu.giof.poc.rest.dto.PaymentStatus;
import eu.giof.poc.service.Configuration;
import eu.giof.poc.service.cache.AccountCache;
import eu.giof.poc.service.cache.BalanceSlotCache;
import eu.giof.poc.service.structure.Account;
import eu.giof.poc.service.structure.BalanceSlot;
import eu.giof.poc.service.structure.BalanceSlotKey;

@RestController
public class PaymentServiceImpl implements PaymentService {
	
	@Autowired
	private AccountCache accountCache;

	@Autowired
	private BalanceSlotCache balanceSlotCache;
	
	@Autowired
	private Configuration configuration;
	
	private final Map<LockOrder, List<BiFunction<PaymentServiceImpl, PaymentServiceContext, Boolean>>> actionMap = new HashMap<>();
	private final List<ValidationStep> validationStepArray = new ArrayList<>();
	
	@PostConstruct
	private void postConstruct() {
		newList(LockOrder.PAYEE_FIRST).addAll(Arrays.asList(
			PaymentServiceImpl::doPayeeLock, 
			PaymentServiceImpl::doPayerLock));
		newList(LockOrder.PAYER_FIRST).addAll(Arrays.asList(
			PaymentServiceImpl::doPayerLock, 
			PaymentServiceImpl::doPayeeLock));
		validationStepArray.addAll(createValidationStepList());
	}
	
	private List<BiFunction<PaymentServiceImpl, PaymentServiceContext, Boolean>> newList(LockOrder lockOrder) {
		List<BiFunction<PaymentServiceImpl, PaymentServiceContext, Boolean>> list = new ArrayList<>();
		actionMap.put(lockOrder, list);
		return list;
	}
	
	private List<ValidationStep> createValidationStepList() {
		return Arrays.asList(
			ValidationStep.create(notEmpty(PaymentBody::getPayerAccountId),	() -> PaymentStatus.UNSPECIFIED_PAYER_ACCOUNT),
			ValidationStep.create(notEmpty(PaymentBody::getPayeeAccountId),	() -> PaymentStatus.UNSPECIFIED_PAYEE_ACCOUNT),
			ValidationStep.create(differentAccounts(), () -> PaymentStatus.SAME_ACCOUNT),
			ValidationStep.create(validAmount(), () -> PaymentStatus.INVALID_AMOUNT),
			ValidationStep.create(loadAccount(PaymentBody::getPayerAccountId, PaymentServiceContext::setPayerAccount), () -> PaymentStatus.PAYER_ACCOUNT_NOT_FOUND),
			ValidationStep.create(loadAccount(PaymentBody::getPayeeAccountId, PaymentServiceContext::setPayeeAccount), () -> PaymentStatus.PAYEE_ACCOUNT_NOT_FOUND));
	}
	
	private void executeValidation(PaymentServiceContext context) {
		Iterator<ValidationStep> iterator = validationStepArray.iterator();
		while (context.isValidationSuccessful() && iterator.hasNext()) {
			executeValidationStep(context, iterator.next());
		}
	}
	
	private void executeValidationStep(
			PaymentServiceContext context, 
			ValidationStep validationStep) {
		Boolean validatorResult = validationStep.getValidator().apply(context);
		boolean validatorSuccessful = Boolean.TRUE.equals(validatorResult);
		context.setValidationSuccessful(validatorSuccessful);
		if (!validatorSuccessful) {
			context.setPaymentStatus(validationStep.getWhenValidatorFails().get());
		}
	}
	
	private Function<PaymentServiceContext, Boolean> notEmpty(Function<PaymentBody, String> stringExtractor) {
		return ctx -> Optional.ofNullable(stringExtractor.apply(ctx.getPaymentBody()))
			.map(acc -> acc.length() > 0)
			.orElse(false);
	}
	
	private Function<PaymentServiceContext, Boolean> differentAccounts() {
		return new Function<PaymentServiceContext, Boolean>() {
			
			@Override
			public Boolean apply(PaymentServiceContext t) {
				return !t.getPaymentBody().getPayeeAccountId().equals(t.getPaymentBody().getPayerAccountId());
			}
		};
	}
	
	private Function<PaymentServiceContext, Boolean> loadAccount(
			Function<PaymentBody, String> accountIdExtractor,
			BiConsumer<PaymentServiceContext, Account> accountConsumer) {
		return new Function<PaymentServiceContext, Boolean>() {

			@Override
			public Boolean apply(PaymentServiceContext context) {
				Account account = accountCache.get(accountIdExtractor.apply(context.getPaymentBody()));
				if (account != null) {
					accountConsumer.accept(context, account);
				}
				return account != null;
			}
		};
	}
	
	private Function<PaymentServiceContext, Boolean> validAmount() {
		return ctx -> Optional.ofNullable(ctx.getPaymentBody())
			.map(b -> b.getAmount()) 
			.map(am -> am.floatValue() > 0.0f)
			.orElse(false);
	}
	
	private PaymentResultDto toResultDto(PaymentServiceContext context) {
		return PaymentResultDto.valueOf(
			context.getPaymentStatus(), 
			context.getPaymentBody().getPayerAccountId(), 
			context.getPaymentBody().getPayeeAccountId(), 
			context.getPaymentBody().getAmount());
	}
	
	private void threadLog(String logString) {
		System.out.println(String.format("Thread [%d] %s", Thread.currentThread().getId(), logString));
	}
	
	void execDoWait(PaymentBody body, Function<PaymentBody, Integer> waitTimeExtractor, String waitName) {
		int requestedSec = Optional.ofNullable(waitTimeExtractor.apply(body)).orElse(0);
		if (requestedSec > 0) {
			threadLog(String.format("Waiting (%s) for %d sec", waitName, requestedSec));
		}
		doWait(requestedSec);
	}
	
	private LockOrder getLockOrder(PaymentServiceContext context) {
		return context.getPayerAccount().getId().compareTo(context.getPayeeAccount().getId()) < 0 
			? LockOrder.PAYER_FIRST
			: LockOrder.PAYEE_FIRST;
	}
	
	private boolean lockSlots(PaymentServiceContext context) {
		LockOrder lockOrder = getLockOrder(context);
		context.setLockOrder(lockOrder);
		List<BiFunction<PaymentServiceImpl, PaymentServiceContext, Boolean>> lockerList = actionMap.get(lockOrder);
		boolean success = true;
		for (int i = 0; success && i < lockerList.size(); ++i) {
			success = lockerList.get(i).apply(this, context);
		}
		return success;
	}
	
	private boolean doPayerLock(PaymentServiceContext context) {
		int i = 0;
		boolean success = false;
		LockActorSlotResult lockActorSlotResult = null;
		// Optimistic lock first
		while (!success && i <= configuration.getOptimisticLockTryCount()) {
			LockActorSlotResult currentLockActorSlotResult = doOptimisticPayerLock(
				context.getPayerAccount().getId(), 
				context.getPaymentBody().getAmount(),
				(k) -> balanceSlotCache.tryLock(k), 
				(k) -> {
					balanceSlotCache.unlock(k);
					return true;
				});
			if (!currentLockActorSlotResult.isSuccess()) {
				// unlock locked slots when optimistic lock was not successful
				for (ActorSlot actorSlot : currentLockActorSlotResult.getActorSlotList()) {
					balanceSlotCache.unlock(actorSlot.getBalanceSlotKey());
				}
			} else {
				success = true;
				lockActorSlotResult = currentLockActorSlotResult;
			}
			++i;
		}
		if (success) {
			storeInContext(context, lockActorSlotResult);
			lockActorSlotResult.getActorSlotList().forEach(x -> logLockBalanceSlotKey(x.getBalanceSlotKey()));
		} else {
			success = doPessimisticPayerLock(context, success);
		}
		return success;
	}

	private boolean doPessimisticPayerLock(PaymentServiceContext context, boolean success) {
		// pessimistic lock, might take some time to lock all
		LockActorSlotResult pessimisticLockActorSlotResult = doOptimisticPayerLock(
			context.getPayerAccount().getId(), 
			context.getPaymentBody().getAmount(),
			(k) -> balanceSlotCache.lock(k), 
			(k) -> {
				balanceSlotCache.unlock(k);
				return true;
			});
		// if not successful -> INSUFFICIENT FUNDS
		if (!pessimisticLockActorSlotResult.isSuccess()) {
			context.setPaymentStatus(PaymentStatus.NOT_ENOUGH_FUNDS);
		} else {
			// OK! store keys and slots in context
			success = true;
			storeInContext(context, pessimisticLockActorSlotResult);
			pessimisticLockActorSlotResult.getActorSlotList().forEach(x -> logLockBalanceSlotKey(x.getBalanceSlotKey()));
		}
		return success;
	}
	
	private void storeInContext(
			PaymentServiceContext context, 
			LockActorSlotResult lockActorSlotResult) {
		// store keys and slots in context
		for (ActorSlot actorSlot : lockActorSlotResult.getActorSlotList()) {
			context.addBalanceSlotKey(actorSlot.getBalanceSlotKey());
			context.addBalanceSlot(actorSlot.getBalanceSlot());
		}
	}

	private boolean doPayeeLock(PaymentServiceContext context) {
		BalanceSlot payeeSlot = null;
		while (payeeSlot == null) {
			int slotId = 1;
			BalanceSlot currentSlot = null;
			boolean lastFound = false;
			boolean unlockedFound = false;
			do {
				BalanceSlotKey currentKey = BalanceSlotKey.valueOf(context.getPayeeAccount().getId(), slotId);
				currentSlot = balanceSlotCache.get(currentKey);
				if (currentSlot != null) {
					// try lock...
					if (balanceSlotCache.tryLock(currentKey)) {
						// found one unlocked!
						logLockBalanceSlotKey(currentKey);						
						payeeSlot = currentSlot;
						context.setPayeeSlot(currentSlot);
						unlockedFound = true;
						context.addBalanceSlotKey(currentKey);
					} else {
						++slotId;
					}
				} else {
					lastFound = true;
				}
			} while (!unlockedFound && !lastFound);
		}
		return true;
	}
	
	@Override
	@PostMapping(value = "payment/pay")
	public PaymentResultDto pay(@RequestBody PaymentBody paymentBody) {
		threadLog("Payment started");
		PaymentServiceContext context = new PaymentServiceContext(paymentBody);
		executeValidation(context);
		if (context.isValidationSuccessful()) {
			// and away we go!
			// lock (respecting lockOrder)
			if (lockSlots(context)) {
				// action, move money
				threadLog("Moving funds");
				execDoWait(paymentBody, PaymentBody::getWaitBeforeMoveSec, "Before Move");
				moveFunds(context);
				execDoWait(paymentBody, PaymentBody::getWaitAfterMoveSec, "After Move");
				context.setPaymentStatus(PaymentStatus.OK);
			} else {
				// failure in context
			}
			// anyway, do all the slot unlock...
			context.getBalanceSlotKeyList().forEach(x -> balanceSlotCache.unlock(x));
		}
		return toResultDto(context);
	}

	private void moveFunds(PaymentServiceContext context) {
		BalanceSlot payeeSlot = context.getPayeeSlot();
		Double amountToBeRemoved = context.getPaymentBody().getAmount();
		for (BalanceSlot slot : context.getBalanceSlotList()) {
			if (slot.getAvailableBalance() <= amountToBeRemoved) {
				// totally remove
				Double slotBalance = slot.getAvailableBalance();
				slot.setAvailableBalance(Double.valueOf(0.0f));
				amountToBeRemoved -= slotBalance;
			} else {
				// partially remove
				slot.setAvailableBalance(slot.getAvailableBalance() - amountToBeRemoved);
				amountToBeRemoved = Double.valueOf(0.0f);
			}
		}
		// give balance to the payee
		payeeSlot.setAvailableBalance(payeeSlot.getAvailableBalance() + context.getPaymentBody().getAmount());
	}
	
	private LockActorSlotResult doOptimisticPayerLock(
			String payerAccountId, 
			Double amountToBeLocked,
			Function<BalanceSlotKey, Boolean> lockFunction,
			Function<BalanceSlotKey, Boolean> unlockFunction) {
		Double lockedAvailableAmount = Double.valueOf(0.0f);
		List<ActorSlot> actorSlotList = new ArrayList<>();
		int i = 1;
		BalanceSlot slot = null;
		do {
			BalanceSlotKey key = BalanceSlotKey.valueOf(payerAccountId, i);
			slot = balanceSlotCache.get(key);
			if (slot != null) {
				if (lockFunction.apply(key)) {
					if (slot.getAvailableBalance() > 0.0f) {
						// accumulate amount
						lockedAvailableAmount += slot.getAvailableBalance();
						ActorSlot actorSlot = ActorSlot.valueOf(slot, key);
						actorSlotList.add(actorSlot);
					} else {
						unlockFunction.apply(key);
					}
				}
			}
			++i;
		} while (
			lockedAvailableAmount < amountToBeLocked && 
			slot != null && 
			i < Integer.MAX_VALUE);
		boolean success = lockedAvailableAmount >= amountToBeLocked;
		return LockActorSlotResult.valueOf(success, actorSlotList);
	}

	private void doWait(Integer waitSec) {
		// sleep if requested
		if (waitSec != null && waitSec.intValue() > 0) {
			try {
				Thread.sleep(waitSec * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void logLockBalanceSlotKey(BalanceSlotKey currentKey) {
		threadLog(String.format("Locked %s %s Id [%s] Slot [%d]",
			BalanceSlot.class.getSimpleName(),
			Account.class.getSimpleName(),
			currentKey.getAccountId(),
			currentKey.getSlotId()));
	}
}
 
