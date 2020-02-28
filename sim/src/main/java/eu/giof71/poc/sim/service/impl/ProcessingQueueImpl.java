package eu.giof71.poc.sim.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import eu.giof71.poc.sim.service.ProcessingQueue;

@Component
public class ProcessingQueueImpl implements ProcessingQueue {
	
	private final List<Object> queue = new LinkedList<>();

	@Override
	public Object pop() {
		synchronized(queue) {
			return Optional.of(queue)
				.filter(q -> !q.isEmpty())
				.map(q -> q.remove(0))
				.orElse(null);
		}
	}

	@Override
	public void push(Object o) {
		synchronized(queue) {
			queue.add(o);
		}
	}

}
