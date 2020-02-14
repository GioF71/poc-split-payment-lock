package eu.sia.poc.rest;

import eu.sia.poc.rest.body.AddAccount;
import eu.sia.poc.rest.dto.AddAccountDto;

public interface AccountService {
	Integer count();
	AddAccountDto add(AddAccount addAccount);
	Double getBalance(String accountId);
}
