package eu.sia.poc.service.structure;

import java.util.Calendar;
import java.util.UUID;

public class Payment {
	
	private final String id = UUID.randomUUID().toString();
	private final String fromAccount;
	private final String toAccount;
	private final Double amount;
	private PaymentStatus status = PaymentStatus.PENDING;
	
	private final Calendar creationTimestamp = Calendar.getInstance();
	private Calendar updateTimestamp = creationTimestamp;
	
	public Payment(
			String fromAccount, 
			String toAccount, 
			Double amount) {
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.amount = amount;
	}

	public String getId() {
		return id;
	}

	public String getFromAccount() {
		return fromAccount;
	}

	public String getToAccount() {
		return toAccount;
	}

	public Double getAmount() {
		return amount;
	}

	public Calendar getCreationTimestamp() {
		return creationTimestamp;
	}

	public Calendar getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Calendar updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public PaymentStatus getStatus() {
		return status;
	}
}
