package eu.giof71.poc.sim.processor.impl;

import org.springframework.stereotype.Component;

import eu.giof71.poc.sim.processor.Worker;
import eu.giof71.poc.sim.service.PaymentInstruction;

@Component
public class WorkerImpl implements Worker {

	@Override
	public void process(PaymentInstruction paymentInstruction) {
		System.out.println(String.format("%s (%d [%s]) is processing one %s", 
			Worker.class.getSimpleName(),
			Thread.currentThread().getId(),
			Thread.currentThread().getName(),
			PaymentInstruction.class.getSimpleName()));
	}
}
