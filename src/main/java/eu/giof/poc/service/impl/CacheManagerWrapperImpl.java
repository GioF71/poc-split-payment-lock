package eu.giof.poc.service.impl;

import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import eu.giof.poc.service.CacheManagerWrapper;
import eu.giof.poc.service.cache.AccountCache;
import eu.giof.poc.service.cache.BalanceSlotCache;
import eu.giof.poc.service.structure.BalanceSlot;
import eu.giof.poc.service.structure.BalanceSlotKey;

@Component
@Scope(value = "singleton")
public class CacheManagerWrapperImpl implements CacheManagerWrapper {

	@Autowired
	private AccountCache accountCache;

	@Autowired
	private BalanceSlotCache balanceSlotCache;
	
	@PostConstruct
	private void postConstruct() throws InterruptedException, ExecutionException {
		System.out.println(String.format("%s %s", getClass().getSimpleName(), PostConstruct.class.getSimpleName()));
		balanceSlotCache.put(BalanceSlotKey.valueOf("1", 1), BalanceSlot.valueOf(Double.valueOf(10.0f)));
		balanceSlotCache.put(BalanceSlotKey.valueOf("1", 2), BalanceSlot.valueOf(Double.valueOf(10.0f)));
		balanceSlotCache.put(BalanceSlotKey.valueOf("1", 3), BalanceSlot.valueOf(Double.valueOf(10.0f)));
		int sz = balanceSlotCache.size();
		System.out.println(sz);
	}

	@Override
	public AccountCache getAccountCache() {
		return accountCache;
	}

	@Override
	public BalanceSlotCache getBalanceSlotCache() {
		return balanceSlotCache;
	}
	
	/*
	Override
	public void getTransaction() {
		try {
			TransactionManager txnManager = cacheManager.getCache(txCache).getAdvancedCache().getTransactionManager();
			if (txnManager.getStatus() != Status.STATUS_ACTIVE)
				txnManager.begin();
		} catch (NotSupportedException | SystemException e) {
			LOGGER.error(e.getMessage());
		}
	} 
	
	@Override
	public void lockKey(String cacheName, Object key) throws Exception {
		if (!cacheManager.getCache(cacheName).getAdvancedCache().lock(key)) {
			LOGGER.error("Fail to lock key [" + key + "] on " + cacheName);
		}

		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("LocksHeld on {}: [{}]", cacheName, cacheManager.getCache(cacheName).getAdvancedCache().withFlags(Flag.SKIP_CACHE_LOAD, Flag.IGNORE_RETURN_VALUES).getLockManager().getNumberOfLocksHeld());
		}
	} 
	*/
}
