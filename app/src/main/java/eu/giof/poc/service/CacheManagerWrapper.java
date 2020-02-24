package eu.giof.poc.service;

import eu.giof.poc.service.cache.AccountCache;
import eu.giof.poc.service.cache.BalanceSlotCache;

public interface CacheManagerWrapper {
	AccountCache getAccountCache();
	BalanceSlotCache getBalanceSlotCache();
//	Cache<String, Payment> getPaymentCache();
}
