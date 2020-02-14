package eu.sia.poc.rest.dto;

import lombok.Getter;

@Getter
public class PaymentResultDto {
	
	private final PaymentStatus paymentStatus;
	private final String payerAccountId;
	private final String payeeAccountId;
	private final Double amount;

	public PaymentResultDto(
			PaymentStatus paymentStatus, 
			String payerAccountId, 
			String payeeAccountId, 
			Double amount) {
		this.paymentStatus = paymentStatus;
		this.payerAccountId = payerAccountId;
		this.payeeAccountId = payeeAccountId;
		this.amount = amount;
	}
}
