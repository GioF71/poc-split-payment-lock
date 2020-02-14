package eu.sia.poc.rest;

import eu.sia.poc.rest.body.PaymentBody;

public interface PaymentService {
	boolean pay(PaymentBody payment);
}
