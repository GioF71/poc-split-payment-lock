package eu.giof.poc.rest.impl.payment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.giof.poc.rest.body.PaymentBody;
import eu.giof.poc.rest.dto.PaymentStatus;
import eu.giof.poc.service.structure.Account;
import eu.giof.poc.service.structure.BalanceSlot;
import eu.giof.poc.service.structure.BalanceSlotKey;
import lombok.Getter;
import lombok.Setter;

class PaymentServiceContext {
	
	@Getter
	private final PaymentBody paymentBody;
	
	@Getter
	@Setter
	private boolean validationSuccessful = true;
	
	@Getter
	@Setter
	private PaymentStatus paymentStatus = null;
	
	@Getter
	@Setter
	private Account payerAccount;
	
	@Getter
	@Setter
	private Account payeeAccount;
	
	@Getter
	@Setter
	private LockOrder lockOrder;
	
	@Getter
	@Setter
	private BalanceSlot payeeSlot;
	
	private final List<BalanceSlotKey> lockList = new ArrayList<>();
	private final List<BalanceSlot> slotList = new ArrayList<>();
	
	PaymentServiceContext(PaymentBody paymentBody) {
		this.paymentBody = paymentBody;
	}
	
	List<BalanceSlotKey> getBalanceSlotKeyList() {
		return Collections.unmodifiableList(lockList);
	}
	
	void addBalanceSlotKey(BalanceSlotKey balanceSlotKey) {
		lockList.add(balanceSlotKey);
	}
	
	List<BalanceSlot> getBalanceSlotList() {
		return Collections.unmodifiableList(slotList);
	}
	
	void addBalanceSlot(BalanceSlot balanceSlot) {
		slotList.add(balanceSlot);
	}
}
