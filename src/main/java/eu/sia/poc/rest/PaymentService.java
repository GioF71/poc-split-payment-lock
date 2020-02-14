package eu.sia.poc.rest;

import eu.sia.poc.rest.body.PaymentBody;
import eu.sia.poc.rest.dto.PaymentResultDto;

public interface PaymentService {
	PaymentResultDto pay(PaymentBody payment);
}
