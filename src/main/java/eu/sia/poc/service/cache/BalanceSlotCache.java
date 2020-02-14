package eu.sia.poc.service.cache;

import eu.sia.poc.service.structure.BalanceSlot;
import eu.sia.poc.service.structure.BalanceSlotKey;

public interface BalanceSlotCache 
	extends CacheInterface<BalanceSlotKey, BalanceSlot> {

	Integer getLastSlotKey(String accountId);
}
