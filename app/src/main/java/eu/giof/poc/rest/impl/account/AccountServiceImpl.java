package eu.giof.poc.rest.impl.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import eu.giof.poc.rest.AccountService;
import eu.giof.poc.rest.body.AddAccount;
import eu.giof.poc.rest.dto.AccountDto;
import eu.giof.poc.rest.dto.AccountSlotListDto;
import eu.giof.poc.rest.dto.AddAccountDto;
import eu.giof.poc.rest.dto.AddResult;
import eu.giof.poc.rest.dto.BalanceDto;
import eu.giof.poc.rest.dto.BalanceSlotDto;
import eu.giof.poc.rest.dto.GetAccountSlotListDto;
import eu.giof.poc.service.Configuration;
import eu.giof.poc.service.cache.AccountCache;
import eu.giof.poc.service.cache.BalanceSlotCache;
import eu.giof.poc.service.structure.Account;
import eu.giof.poc.service.structure.BalanceSlot;
import eu.giof.poc.service.structure.BalanceSlotKey;

@RestController
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountCache accountCache;

	@Autowired
	private BalanceSlotCache balanceSlotCache;
		
	@Autowired
	private Configuration configuration;
	
	@Override
	@GetMapping(value = "/account/balance/{accountId}")
	public BalanceDto getBalance(@PathVariable String accountId) {
		BalanceDto dto = null;
		// is lock needed?
		boolean exists = accountCache.containsKey(accountId);
		if (exists) {
			Double balance = Double.valueOf(0.0f);
			Integer currentSlotId = 1;
			BalanceSlot currentSlot = balanceSlotCache.get(BalanceSlotKey.valueOf(accountId, 1));
			while (currentSlotId < Integer.MAX_VALUE && currentSlot != null) {
				balance += currentSlot.getAvailableBalance();
				++currentSlotId;
				currentSlot = balanceSlotCache.get(BalanceSlotKey.valueOf(accountId, currentSlotId));
			}		
			dto = BalanceDto.found(accountId, balance);
		} else {
			dto = BalanceDto.notFound(accountId);
		}
		return dto;
	}

	@Override
	@GetMapping(value = "/account/slotlist/{accountId}")
	public GetAccountSlotListDto getSlotList(@PathVariable String accountId) {
		GetAccountSlotListDto dto = null;
		// is lock needed?
		boolean exists = accountCache.containsKey(accountId);
		if (exists) {
			AccountSlotListDto accountSlotListDto = AccountSlotListDto.create(accountId);
			Integer currentSlotId = 1;
			BalanceSlot currentSlot = balanceSlotCache.get(BalanceSlotKey.valueOf(accountId, 1));
			while (currentSlotId < Integer.MAX_VALUE && currentSlot != null) {
				currentSlot = balanceSlotCache.get(BalanceSlotKey.valueOf(accountId, currentSlotId));
				if (currentSlot != null) {
					accountSlotListDto.add(BalanceSlotDto.valueOf(currentSlotId, currentSlot.getAvailableBalance()));
				}
				++currentSlotId;
			}
			dto = GetAccountSlotListDto.found(accountId, accountSlotListDto);
		} else {
			dto = GetAccountSlotListDto.notFound(accountId);
		}
		return dto;
	}

	@Override
	@GetMapping(value = "/account/count")
	public Integer count() {
		// no need to lock
		return accountCache.size();
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
	@PostMapping(value = "/account/add")
	public AddAccountDto add(@RequestBody AddAccount addAccount) {
		String accountId = addAccount.getId();
		String name = addAccount.getName();
		accountCache.lock();
		Account existing = accountCache.get(accountId);
		if (existing != null) {
			accountCache.unlock();
			return AddAccountDto.alreadyExists();
		} else {
			Account newAccount = Account.valueOf(accountId, name);
			accountCache.put(accountId, newAccount);
			int slotCount = getSlotCount(addAccount);
			Double accountBalance = getBalance(addAccount);
			for (int slotIndex = 0; slotIndex < slotCount; ++slotIndex) {
				BalanceSlotKey currentSlotKey = BalanceSlotKey.valueOf(accountId, slotIndex + 1);
				BalanceSlot currentSlot = BalanceSlot.valueOf(accountBalance / slotCount);
				balanceSlotCache.put(currentSlotKey, currentSlot);
			}
			accountCache.unlock();
			AccountDto accountDto = AccountDto.valueOf(accountId, name, addAccount.getBalance(), addAccount.getSlotCount());
			return AddAccountDto.valueOf(
				AddResult.ADD_OK, 
				accountDto);
		}
	}

	@Override
	@PostMapping(value = "/account/rebalance/{accountId}")
	public GetAccountSlotListDto rebalance(@PathVariable String accountId) {
		GetAccountSlotListDto dto = null;
		accountCache.lock();
		Account existing = accountCache.get(accountId);
		if (existing != null) {
			AccountSlotListDto accountSlotListDto = AccountSlotListDto.create(accountId);
			// find and lock slots
			Double totalBalance = Double.valueOf(0.0f);
			List<BalanceSlot> slotList = new ArrayList<>();
			List<BalanceSlotKey> slotKeyList = new ArrayList<>();
			int slotId = 1;
			BalanceSlot slot = null;
			do {
				BalanceSlotKey key = BalanceSlotKey.valueOf(accountId, slotId);
				slot = balanceSlotCache.get(key);
				if (slot != null) {
					slotList.add(slot);
					slotKeyList.add(key);
					balanceSlotCache.lock(key);
					totalBalance += Optional.ofNullable(slot.getAvailableBalance()).orElse(Double.valueOf(0.0f));
				}
				++slotId;
			} while (slot != null && slotId < Integer.MAX_VALUE);
			// rebalancing and unlock
			for (int i = 0; i < slotList.size(); ++i) {
				slotList.get(i).setAvailableBalance(totalBalance / slotList.size());
				balanceSlotCache.unlock(slotKeyList.get(i));
				accountSlotListDto.add(BalanceSlotDto.valueOf(i + 1, totalBalance / slotList.size()));
			}
			dto = GetAccountSlotListDto.found(accountId, accountSlotListDto);
		} else {
			dto = GetAccountSlotListDto.notFound(accountId);
		}
		accountCache.unlock();
		return dto;
	}
}
