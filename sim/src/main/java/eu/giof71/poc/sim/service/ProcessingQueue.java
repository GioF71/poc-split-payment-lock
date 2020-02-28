package eu.giof71.poc.sim.service;

public interface ProcessingQueue {
	Object pop();
	void push(Object o);
}
