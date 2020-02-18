package eu.giof.poc.service.cache.abs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class CacheItem<V> {

	private final V value;
	private final Lock lock = new ReentrantLock(/* fair */ true);

	CacheItem(V value) {
		this.value = value;
	}

	V getValue() {
		return value;
	}

	void lock() {
		lock.lock();
	}

	boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return lock.tryLock(time, unit);
	}

	boolean tryLock() {
		return lock.tryLock();
	}

	void unlock() {
		lock.unlock();
	}
}
