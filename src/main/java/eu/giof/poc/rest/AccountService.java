package eu.giof.poc.rest;

import eu.giof.poc.rest.body.AddAccount;
import eu.giof.poc.rest.dto.AddAccountDto;
import eu.giof.poc.rest.dto.GetAccountSlotListDto;

public interface AccountService {
	Integer count();
	AddAccountDto add(AddAccount addAccount);
	Double getBalance(String accountId);
	GetAccountSlotListDto getSlotList(String accountId);
	GetAccountSlotListDto rebalance(String accountId);
}
