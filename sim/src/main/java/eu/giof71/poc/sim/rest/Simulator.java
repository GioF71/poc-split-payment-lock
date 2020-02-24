package eu.giof71.poc.sim.rest;

import eu.giof71.poc.sim.rest.body.PaymentRequest;
import eu.giof71.poc.sim.rest.body.TestBed;
import eu.giof71.poc.sim.rest.dto.PrepareTestBedResult;
import eu.giof71.poc.sim.service.PaymentInstruction;

public interface Simulator {
	PrepareTestBedResult prepare(TestBed testBed);
	PaymentInstruction addPaymentRequest(PaymentRequest request);
	int pendingPaymentRequestCount();
	
}
