package eu.sia.poc.rest.impl;

import java.util.function.Function;
import java.util.function.Supplier;

import eu.sia.poc.rest.dto.PaymentStatus;
import io.reactivex.annotations.NonNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class ValidationStep {
	
	static ValidationStep create(
			Function<PaymentServiceContext, Boolean> validator, 
			Supplier<PaymentStatus> whenValidatorFails) {
		return new ValidationStep(validator, whenValidatorFails);
	}
	
	@NonNull
	@Getter
	private final Function<PaymentServiceContext, Boolean> validator;
	
	@NonNull
	@Getter
	private final Supplier<PaymentStatus> whenValidatorFails;
};
