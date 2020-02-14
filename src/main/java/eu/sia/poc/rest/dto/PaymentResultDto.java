package eu.sia.poc.rest.dto;

public class PaymentResultDto {
	
	private PaymentStatus paymentStatus;
	private String payerAccountId;
	private String payeeAccountId;
	private Double amount;

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPayerAccountId() {
		return payerAccountId;
	}

	public void setPayerAccountId(String payerAccountId) {
		this.payerAccountId = payerAccountId;
	}

	public String getPayeeAccountId() {
		return payeeAccountId;
	}

	public void setPayeeAccountId(String payeeAccountId) {
		this.payeeAccountId = payeeAccountId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
