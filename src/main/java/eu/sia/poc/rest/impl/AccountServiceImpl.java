package eu.sia.poc.rest.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import eu.sia.poc.rest.AccountService;
import eu.sia.poc.rest.body.AddAccount;
import eu.sia.poc.rest.dto.AccountDto;
import eu.sia.poc.rest.dto.AddAccountDto;
import eu.sia.poc.rest.dto.AddResult;
import eu.sia.poc.service.CacheManagerWrapper;
import eu.sia.poc.service.cache.AccountCache;
import eu.sia.poc.service.cache.BalanceSlotCache;
import eu.sia.poc.service.structure.Account;
import eu.sia.poc.service.structure.BalanceSlot;
import eu.sia.poc.service.structure.BalanceSlotKey;

@RestController
public class AccountServiceImpl implements AccountService {

	@Autowired
	private CacheManagerWrapper cacheManagerWrapper;

	@Autowired
	private BalanceSlotCache balanceSlotCache;
	
	@Override
	@GetMapping(value = "/account/balance/{accountId}")
	public Double getBalance(@PathVariable String accountId) {
		// TODO Auto-generated method stub
		Double balance = Double.valueOf(0.0f);
		Integer currentSlotId = 1;
		BalanceSlot currentSlot = balanceSlotCache.get(BalanceSlotKey.valueOf(accountId, 1));
		while (currentSlotId < Integer.MAX_VALUE && currentSlot != null) {
			balance += currentSlot.getAvailableBalance();
			++currentSlotId;
			currentSlot = balanceSlotCache.get(BalanceSlotKey.valueOf(accountId, currentSlotId));
		}		
		return balance;
	}

	@Override
	@GetMapping(value = "/account/count")
	public Integer count() {
		return cacheManagerWrapper.getAccountCache().size();
	}

	@Override
	@PutMapping(value = "/account/add")
	public AddAccountDto add(@RequestBody AddAccount addAccount) {
		String id = addAccount.getId();
		String name = addAccount.getName();
		AccountCache accountCache = cacheManagerWrapper.getAccountCache();
		BalanceSlotCache balanceSlotCache = cacheManagerWrapper.getBalanceSlotCache();
		Account existing = accountCache.get(id);
		if (existing != null) {
			return new AddAccountDto(
				AddResult.ALREADY_EXISTS, 
				new AccountDto(existing.getId(), existing.getName()));
		} else {
			Account newAccount = new Account(id, name);
			accountCache.put(id, newAccount);
			for (int slotIndex = 0; slotIndex < addAccount.getSlotCount(); ++slotIndex) {
				BalanceSlotKey currentSlotKey = BalanceSlotKey.valueOf(id, slotIndex + 1);
				BalanceSlot currentSlot = new BalanceSlot(addAccount.getBalance() / addAccount.getSlotCount());
				balanceSlotCache.put(currentSlotKey, currentSlot);
			}
			return new AddAccountDto(
				AddResult.ADD_OK, 
				new AccountDto(id, name));
		}
	}
}
