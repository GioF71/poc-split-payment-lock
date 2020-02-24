package eu.giof.poc.service.cache.impl;

import org.springframework.stereotype.Component;

import eu.giof.poc.service.cache.AccountCache;
import eu.giof.poc.service.cache.abs.AbsCache;
import eu.giof.poc.service.structure.Account;

@Component
public class AccountCacheImpl 
	extends AbsCache<String, Account> 
	implements AccountCache {
}
