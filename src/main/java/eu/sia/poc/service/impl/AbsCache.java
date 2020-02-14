package eu.sia.poc.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import eu.sia.poc.service.cache.CacheInterface;

public class AbsCache<K, V> implements CacheInterface<K, V> {

	private Map<K, CacheItem<V>> map = new HashMap<>();

	protected Map<K, CacheItem<V>> getMap() {
		return map;
	}
	
	@Override
	public boolean tryLock(K key) {
		CacheItem<V> item = map.get(key);
		if (item != null) {
			return item.tryLock();
		} else {
			return false;
		}
	}

	@Override
	public void unlock(K key) {
		CacheItem<V> item = map.get(key);
		if (item != null) {
			item.unlock();
		} 
	}

	@Override
	public boolean containsKey(K key) {
		return map.containsKey(key);
	}

	@Override
	public V put(K key, V value) {
		CacheItem<V> result = map.put(key, new CacheItem<>(value));
		return Optional.ofNullable(result)
			.map(CacheItem::getValue)
			.orElse(null);
	}

	@Override
	public V get(K key) {
		return Optional.ofNullable(map.get(key))
			.map(CacheItem::getValue)
			.orElse(null);
	}

	@Override
	public int size() {
		return map.size();
	}
}
