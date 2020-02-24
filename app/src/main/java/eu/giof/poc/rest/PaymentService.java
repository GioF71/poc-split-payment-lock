package eu.giof.poc.rest;

import eu.giof.poc.rest.body.PaymentBody;
import eu.giof.poc.rest.dto.PaymentResultDto;

public interface PaymentService {
	PaymentResultDto pay(PaymentBody payment);
}
