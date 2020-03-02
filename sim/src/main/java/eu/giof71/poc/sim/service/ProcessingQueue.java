package eu.giof71.poc.sim.service;

import java.util.concurrent.TimeUnit;

public interface ProcessingQueue {
	int depth();
	PaymentInstruction tryPop(long time, TimeUnit unit) throws InterruptedException;
	PaymentInstruction pop();
	void push(PaymentInstruction o);
}
