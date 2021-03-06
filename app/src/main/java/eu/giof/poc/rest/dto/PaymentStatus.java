package eu.giof.poc.rest.dto;

public enum PaymentStatus {
	PENDING,
	OK,
	UNSPECIFIED_PAYER_ACCOUNT,
	UNSPECIFIED_PAYEE_ACCOUNT,
	SAME_ACCOUNT,
	INVALID_AMOUNT,
	PAYER_ACCOUNT_NOT_FOUND,
	PAYEE_ACCOUNT_NOT_FOUND,
	NOT_ENOUGH_FUNDS,
	FAILED,
	TIMED_OUT
}
