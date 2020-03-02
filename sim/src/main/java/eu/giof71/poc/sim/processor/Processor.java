package eu.giof71.poc.sim.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import eu.giof71.poc.sim.service.Configuration;
import eu.giof71.poc.sim.service.PaymentInstruction;
import eu.giof71.poc.sim.service.ProcessingQueue;

@Component
public class Processor {
	
//	@Autowired
//	private SimulationData simulationData;
	
	@Autowired
	private Configuration configuration;
	
	@Autowired
	private Worker worker;
	
	@Autowired
	private ProcessingQueue processingQueue;

	@Scheduled(fixedDelay = 3000, initialDelayString = "5000")
	public void task() {
		List<Thread> runnableList = new ArrayList<>();
		for (int i = 0; i < configuration.getNumberOfThreads(); ++i) {
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (true) {
						PaymentInstruction instruction;
						try {
							instruction = processingQueue.tryPop(100, TimeUnit.MILLISECONDS);
							if (instruction != null) {
								worker.process(instruction);
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
			Thread currentThread = new Thread(runnable, String.format("Worker %03d", i + 1));
			currentThread.start();
			runnableList.add(currentThread);
		}
		// TODO stop?
//		System.out.println("Processing ...");
//		PaymentInstruction instruction = null;
//		while ((instruction = processingQueue.pop()) != null) {
//			System.out.println(String.format("Processing one %s", PaymentInstruction.class.getSimpleName()));
//			// todo process...
//		}
	}
}
