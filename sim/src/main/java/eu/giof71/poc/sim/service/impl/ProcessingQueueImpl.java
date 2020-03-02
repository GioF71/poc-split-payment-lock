package eu.giof71.poc.sim.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Component;

import eu.giof71.poc.sim.service.PaymentInstruction;
import eu.giof71.poc.sim.service.ProcessingQueue;

@Component
public class ProcessingQueueImpl implements ProcessingQueue {
	
	private final Lock lock = new ReentrantLock(true);
	private final List<PaymentInstruction> queue = new LinkedList<>();

	@Override
	public PaymentInstruction tryPop(long time, TimeUnit unit) throws InterruptedException {
		try {
			PaymentInstruction result = null;
			if (lock.tryLock(time, unit)) {
				result = Optional.of(queue)
					.filter(q -> !q.isEmpty())
					.map(q -> q.remove(0))
					.orElse(null);
			}
			return result;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public PaymentInstruction pop() {
		try {
			lock.lock();
			return Optional.of(queue)
				.filter(q -> !q.isEmpty())
				.map(q -> q.remove(0))
				.orElse(null);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void push(PaymentInstruction o) {
		lock.lock();
		queue.add(o);
		lock.unlock();
	}

	@Override
	public int depth() {
		try {
			lock.lock();
			return queue.size();
		} finally {
			lock.unlock();
		}
	}
}
