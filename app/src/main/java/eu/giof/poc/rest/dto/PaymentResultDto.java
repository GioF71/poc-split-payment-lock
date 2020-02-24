package eu.giof.poc.rest.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentResultDto {
	
	private final PaymentStatus paymentStatus;
	private final String payerAccountId;
	private final String payeeAccountId;
	private final Double amount;
	
	public static PaymentResultDto valueOf(
			PaymentStatus paymentStatus, 
			String payerAccountId, 
			String payeeAccountId, 
			Double amount) {
		return new PaymentResultDto(
			paymentStatus, 
			payerAccountId, 
			payeeAccountId, 
			amount);
	}
}
