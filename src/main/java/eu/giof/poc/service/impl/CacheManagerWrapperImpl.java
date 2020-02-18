package eu.giof.poc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.giof.poc.service.CacheManagerWrapper;
import eu.giof.poc.service.cache.AccountCache;
import eu.giof.poc.service.cache.BalanceSlotCache;

@Component
public class CacheManagerWrapperImpl implements CacheManagerWrapper {

	@Autowired
	private AccountCache accountCache;

	@Autowired
	private BalanceSlotCache balanceSlotCache;
	
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
	*/
}
