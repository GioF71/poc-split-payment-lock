package eu.giof.poc.service.cache;

import eu.giof.poc.service.structure.BalanceSlot;
import eu.giof.poc.service.structure.BalanceSlotKey;

public interface BalanceSlotCache 
	extends CacheInterface<BalanceSlotKey, BalanceSlot> {
	void lock();
	void unlock();
	Integer getLastSlotKey(String accountId);
}
