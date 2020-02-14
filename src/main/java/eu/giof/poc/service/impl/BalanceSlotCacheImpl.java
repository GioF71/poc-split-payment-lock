package eu.giof.poc.service.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import eu.giof.poc.service.cache.BalanceSlotCache;
import eu.giof.poc.service.structure.BalanceSlot;
import eu.giof.poc.service.structure.BalanceSlotKey;

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
			} else {
				currentSlot = null;
			}
		}
		return result;
	}
}
