package eu.giof.poc.service.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import eu.giof.poc.service.cache.AccountCache;
import eu.giof.poc.service.structure.Account;

@Component
@Scope(value = "singleton")
public class AccountCacheImpl 
	extends AbsCache<String, Account> 
	implements AccountCache {

	
}