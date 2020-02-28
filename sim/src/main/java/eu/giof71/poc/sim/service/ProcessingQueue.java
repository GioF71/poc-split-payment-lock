package eu.giof71.poc.sim.service;

public interface ProcessingQueue {
	int depth();
	PaymentInstruction pop();
	void push(PaymentInstruction o);
}
