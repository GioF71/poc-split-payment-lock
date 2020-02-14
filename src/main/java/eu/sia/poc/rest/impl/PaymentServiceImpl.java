package eu.sia.poc.rest.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import eu.sia.poc.rest.PaymentService;
import eu.sia.poc.rest.body.PaymentBody;
import eu.sia.poc.service.cache.AccountCache;
import eu.sia.poc.service.cache.BalanceSlotCache;
import eu.sia.poc.service.structure.Account;
import eu.sia.poc.service.structure.BalanceSlot;
import eu.sia.poc.service.structure.BalanceSlotKey;

@RestController
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private AccountCache accountCache;

	@Autowired
	private BalanceSlotCache balanceSlotCache;

	@Override
	@PostMapping(value = "payment/pay")
	public boolean pay(@RequestBody PaymentBody payment) {
		boolean result = false;
		Account payerAccount = accountCache.get(payment.getPayerAccountId());
		Account payeeAccount = accountCache.get(payment.getPayeeAccountId());
		List<BalanceSlotKey> lockList = new ArrayList<>();
		List<BalanceSlot> slotList = new ArrayList<>();
		Double lockedAvailableAmount = Double.valueOf(0.0f);
		if (payerAccount != null && payeeAccount != null) {
			// try lock payer slot(s)
			lockedAvailableAmount = lockPayerSlots(
				payment, 
				payerAccount, 
				lockList, 
				slotList);
		}
		if (lockedAvailableAmount >= payment.getAmount()) {
			moveFunds(payment, slotList, lockList);
			wait(payment);
			result = true;
		}
		// any way do the unlock
		lockList.forEach(x -> balanceSlotCache.unlock(x));
		return result;
	}

	private void moveFunds(
			PaymentBody payment, 
			List<BalanceSlot> slotList,
			List<BalanceSlotKey> lockList) {
		// can proceed with payment
		// first, identify slot in payee
		BalanceSlotKey payeeSlotKey = BalanceSlotKey.valueOf(payment.getPayeeAccountId(), 1);
		BalanceSlot payeeSlot = balanceSlotCache.get(payeeSlotKey);
		// lockit (should find the first that can be locked
		// or create a new one
		if (payeeSlot != null && balanceSlotCache.tryLock(payeeSlotKey)) {
			lockList.add(payeeSlotKey);
			Double amountToBeRemoved = payment.getAmount();
			for (BalanceSlot slot : slotList) {
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
			payeeSlot.setAvailableBalance(payeeSlot.getAvailableBalance() + payment.getAmount());
		}
	}

	private Double lockPayerSlots(
			PaymentBody payment, 
			Account payerAccount, 
			List<BalanceSlotKey> lockList,
			List<BalanceSlot> slotList) {
		Double lockedAvailableAmount = Double.valueOf(0.0f);
		int i = 1;
		BalanceSlot slot = null;
		do {
			BalanceSlotKey key = BalanceSlotKey.valueOf(payerAccount.getId(), i);
			slot = balanceSlotCache.get(key);
			if (slot != null) {
				// try to lock
				if (balanceSlotCache.tryLock(key)) {
					lockList.add(key);
					lockedAvailableAmount += slot.getAvailableBalance();
					slotList.add(slot);
				}
			}
			++i;
		} while (lockedAvailableAmount < payment.getAmount() && slot != null && i < Integer.MAX_VALUE);
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
