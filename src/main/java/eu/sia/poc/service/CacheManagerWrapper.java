package eu.sia.poc.service;

import eu.sia.poc.service.cache.AccountCache;
import eu.sia.poc.service.cache.BalanceSlotCache;

public interface CacheManagerWrapper {
	AccountCache getAccountCache();
	BalanceSlotCache getBalanceSlotCache();
//	Cache<String, Payment> getPaymentCache();
}
