package eu.sia.poc.service.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CacheItem<V> {

	private V value;
	private Lock lock = new ReentrantLock();

	public CacheItem(V value) {
		this.value = value;
	}

	public V getValue() {
		return value;
	}

	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return lock.tryLock(time, unit);
	}

	public boolean tryLock() {
		return lock.tryLock();
	}

	public void unlock() {
		lock.unlock();
	}
}
