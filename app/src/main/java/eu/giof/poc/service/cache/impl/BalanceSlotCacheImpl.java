package eu.giof.poc.service.cache.impl;

import org.springframework.stereotype.Component;

import eu.giof.poc.service.cache.BalanceSlotCache;
import eu.giof.poc.service.cache.abs.AbsCache;
import eu.giof.poc.service.structure.BalanceSlot;
import eu.giof.poc.service.structure.BalanceSlotKey;

@Component
public class BalanceSlotCacheImpl 
	extends AbsCache<BalanceSlotKey, BalanceSlot>
	implements BalanceSlotCache {
}
