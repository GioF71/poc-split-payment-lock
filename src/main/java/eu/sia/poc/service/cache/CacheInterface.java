package eu.sia.poc.service.cache;

public interface CacheInterface<K, V> {
	boolean tryLock(K key);
	void unlock(K key);
	boolean containsKey(K key);
	V put(K key, V value);
	V get(K key);
	int size();
}
