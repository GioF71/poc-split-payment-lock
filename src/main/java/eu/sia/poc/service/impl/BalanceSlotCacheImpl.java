package eu.sia.poc.service.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import eu.sia.poc.service.cache.BalanceSlotCache;
import eu.sia.poc.service.structure.BalanceSlot;
import eu.sia.poc.service.structure.BalanceSlotKey;

@Component
@Scope(value = "singleton")
public class BalanceSlotCacheImpl 
	extends AbsCache<BalanceSlotKey, BalanceSlot>
	implements BalanceSlotCache {

	@Override
	public Integer getLastSlotKey(String accountId) {
		BalanceSlot currentSlot = get(BalanceSlotKey.valueOf(accountId, 1));
		Integer result = 1;
		for (int i = 2; currentSlot != null && i < Integer.MAX_VALUE; ++i) {
			BalanceSlot nextSlot = get(BalanceSlotKey.valueOf(accountId, i));
			if (nextSlot != null) {
				result = i;
				currentSlot = nextSlot;
			}
		}
		return result;
	}
}
