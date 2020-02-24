package eu.giof71.poc.sim.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import eu.giof71.poc.sim.service.PaymentInstruction;
import eu.giof71.poc.sim.service.SimulationData;

@Component
public class Processor {
	
	@Autowired
	private SimulationData simulationData;

	@Scheduled(fixedDelay = 3000, initialDelayString = "5000")
	public void task() {
		System.out.println("Processing ...");
		PaymentInstruction instruction = null;
		while ((instruction = simulationData.popPaymentInstruction()) != null) {
			System.out.println(String.format("Processing one %s", PaymentInstruction.class.getSimpleName()));
			// todo process...
		}
	}
}
