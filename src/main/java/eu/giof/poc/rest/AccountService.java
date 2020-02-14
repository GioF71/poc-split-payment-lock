package eu.giof.poc.rest;

import eu.giof.poc.rest.body.AddAccount;
import eu.giof.poc.rest.dto.AddAccountDto;

public interface AccountService {
	Integer count();
	AddAccountDto add(AddAccount addAccount);
	Double getBalance(String accountId);
}
