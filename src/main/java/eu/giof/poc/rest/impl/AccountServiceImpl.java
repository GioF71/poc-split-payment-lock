package eu.giof.poc.rest.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import eu.giof.poc.rest.AccountService;
import eu.giof.poc.rest.body.AddAccount;
import eu.giof.poc.rest.dto.AccountDto;
import eu.giof.poc.rest.dto.AccountSlotListDto;
import eu.giof.poc.rest.dto.AddAccountDto;
import eu.giof.poc.rest.dto.AddResult;
import eu.giof.poc.rest.dto.BalanceSlotDto;
import eu.giof.poc.service.CacheManagerWrapper;
import eu.giof.poc.service.Configuration;
import eu.giof.poc.service.cache.AccountCache;
import eu.giof.poc.service.cache.BalanceSlotCache;
import eu.giof.poc.service.structure.Account;
import eu.giof.poc.service.structure.BalanceSlot;
import eu.giof.poc.service.structure.BalanceSlotKey;

@RestController
public class AccountServiceImpl implements AccountService {

	@Autowired
	private CacheManagerWrapper cacheManagerWrapper;

	@Autowired
	private BalanceSlotCache balanceSlotCache;
	
	@Autowired
	private Configuration configuration;
	
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
	@GetMapping(value = "/account/slotlist/{accountId}")
	public AccountSlotListDto getSlotList(@PathVariable String accountId) {
		AccountSlotListDto dto = AccountSlotListDto.create(accountId);
		Integer currentSlotId = 1;
		BalanceSlot currentSlot = balanceSlotCache.get(BalanceSlotKey.valueOf(accountId, 1));
		while (currentSlotId < Integer.MAX_VALUE && currentSlot != null) {
			currentSlot = balanceSlotCache.get(BalanceSlotKey.valueOf(accountId, currentSlotId));
			if (currentSlot != null) {
				dto.add(BalanceSlotDto.valueOf(currentSlotId, currentSlot.getAvailableBalance()));
			}
			++currentSlotId;
		}		
		return dto;
	}

	@Override
	@GetMapping(value = "/account/count")
	public Integer count() {
		return cacheManagerWrapper.getAccountCache().size();
	}
	
	private Double getBalance(AddAccount addAccount) {
		return Optional.ofNullable(addAccount.getBalance())
			.orElse(Double.valueOf(0.0f));
	}

	
	private int getSlotCount(AddAccount addAccount) {
		int slotCount = Optional.ofNullable(addAccount.getSlotCount())
			.orElse(configuration.getDefaultSlotCount());
		if (slotCount < configuration.getMinSlotCount()) {
			slotCount = configuration.getMinSlotCount();
		} else if (slotCount > configuration.getMaxSlotCount()) {
			slotCount = configuration.getMaxSlotCount();
		}
		return slotCount;
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
			return AddAccountDto.valueOf(
				AddResult.ALREADY_EXISTS, 
				AccountDto.valueOf(existing.getId(), existing.getName()));
		} else {
			Account newAccount = new Account(id, name);
			accountCache.put(id, newAccount);
			int slotCount = getSlotCount(addAccount);
			Double accountBalance = getBalance(addAccount);
			for (int slotIndex = 0; slotIndex < slotCount; ++slotIndex) {
				BalanceSlotKey currentSlotKey = BalanceSlotKey.valueOf(id, slotIndex + 1);
				BalanceSlot currentSlot = BalanceSlot.valueOf(accountBalance / slotCount);
				balanceSlotCache.put(currentSlotKey, currentSlot);
			}
			return AddAccountDto.valueOf(
				AddResult.ADD_OK, 
				AccountDto.valueOf(id, name));
		}
	}
}
