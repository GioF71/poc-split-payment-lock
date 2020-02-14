package eu.giof.poc.rest.impl;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import eu.giof.poc.rest.PaymentService;
import eu.giof.poc.rest.body.PaymentBody;
import eu.giof.poc.rest.dto.PaymentResultDto;
import eu.giof.poc.rest.dto.PaymentStatus;
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
	
	private final ValidationStep validationStepArray[] = new ValidationStep[] {
		ValidationStep.create(notEmpty(PaymentBody::getPayerAccountId),	() -> PaymentStatus.UNSPECIFIED_PAYER_ACCOUNT),
		ValidationStep.create(notEmpty(PaymentBody::getPayeeAccountId),	() -> PaymentStatus.UNSPECIFIED_PAYEE_ACCOUNT),
		ValidationStep.create(validAmount(), () -> PaymentStatus.INVALID_AMOUNT),
		ValidationStep.create(loadAccount(PaymentBody::getPayerAccountId, PaymentServiceContext::setPayerAccount), () -> PaymentStatus.PAYER_ACCOUNT_NOT_FOUND),
		ValidationStep.create(loadAccount(PaymentBody::getPayeeAccountId, PaymentServiceContext::setPayeeAccount), () -> PaymentStatus.PAYEE_ACCOUNT_NOT_FOUND)
	};
	
	private void executeValidation(PaymentServiceContext context) {
		int i = 0;
		while (context.isValidationSuccessful() && i < validationStepArray.length) {
			executeValidationStep(context, validationStepArray[i++]);
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
		return new PaymentResultDto(
			context.getPaymentStatus(), 
			context.getPaymentBody().getPayerAccountId(), 
			context.getPaymentBody().getPayeeAccountId(), 
			context.getPaymentBody().getAmount());
	}
	
	private void threadLog(String logString) {
		System.out.println(String.format("Thread [%d] %s", Thread.currentThread().getId(), logString));
	}
	
	@Override
	@PostMapping(value = "payment/pay")
	public PaymentResultDto pay(@RequestBody PaymentBody paymentBody) {
		threadLog("Payment started");
		PaymentServiceContext context = new PaymentServiceContext(paymentBody);
		executeValidation(context);
		if (context.isValidationSuccessful()) {
			// go on!
			Double lockedAvailableAmount = Double.valueOf(0.0f);
			lockedAvailableAmount = lockPayerSlots(context);
			if (lockedAvailableAmount >= paymentBody.getAmount()) {
				threadLog("Moving funds");
				moveFunds(context);
				wait(paymentBody);
				context.setPaymentStatus(PaymentStatus.OK);
			} else {
				context.setPaymentStatus(PaymentStatus.NOT_ENOUGH_FUNDS);
			}
			// anyway, do the account unlock...
			context.getLockList().forEach(x -> balanceSlotCache.unlock(x));
		}
		return toResultDto(context);
	}

	private void moveFunds(PaymentServiceContext context) {
		// can proceed with payment
		// first, identify next slot in payee
		int lastPayeeSlotId = balanceSlotCache.getLastSlotKey(context.getPayeeAccount().getId());
		BalanceSlotKey payeeSlotKey = BalanceSlotKey.valueOf(context.getPayeeAccount().getId(), lastPayeeSlotId + 1);
		BalanceSlot payeeSlot = new BalanceSlot(Double.valueOf(0.0f));
		balanceSlotCache.put(payeeSlotKey, payeeSlot);
		// TODO define method to put a new item an lock it in one operation
		balanceSlotCache.tryLock(payeeSlotKey);
		context.addToLockList(payeeSlotKey);
			Double amountToBeRemoved = context.getPaymentBody().getAmount();
			for (BalanceSlot slot : context.getSlotList()) {
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
			// give balance to payee
			payeeSlot.setAvailableBalance(payeeSlot.getAvailableBalance() + context.getPaymentBody().getAmount());
		//}
	}

	private Double lockPayerSlots(PaymentServiceContext context) {
		Double lockedAvailableAmount = Double.valueOf(0.0f);
		int i = 1;
		BalanceSlot slot = null;
		do {
			BalanceSlotKey key = BalanceSlotKey.valueOf(context.getPaymentBody().getPayerAccountId(), i);
			slot = balanceSlotCache.get(key);
			if (slot != null) {
				if (balanceSlotCache.tryLock(key)) {
					threadLog(String.format("Locked %s %s Id [%s] Slot [%d]",
						BalanceSlot.class.getSimpleName(),
						Account.class.getSimpleName(),
						key.getAccountId(),
						key.getSlotId()));
					if (slot.getAvailableBalance() > 0.0f) {
						// add to lock list
						context.addToLockList(key);
						// accumulate amount
						lockedAvailableAmount += slot.getAvailableBalance();
						context.addToSlotList(slot);
					} else {
						balanceSlotCache.unlock(key);
					}
				}
			}
			++i;
		} while (
			lockedAvailableAmount < context.getPaymentBody().getAmount() && 
			slot != null && 
			i < Integer.MAX_VALUE);
		return lockedAvailableAmount;
	}

	private void wait(PaymentBody payment) {
		// sleep if requested
		Integer waitSec = payment.getWaitSec();
		if (waitSec != null && waitSec.intValue() > 0) {
			try {
				Thread.sleep(waitSec * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
 
