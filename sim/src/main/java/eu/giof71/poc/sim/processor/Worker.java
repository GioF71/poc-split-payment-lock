package eu.giof71.poc.sim.processor;

import eu.giof71.poc.sim.service.PaymentInstruction;

public interface Worker {
	void process(PaymentInstruction paymentInstruction);
}
