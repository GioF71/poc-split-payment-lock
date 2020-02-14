package eu.sia.poc.rest.dto;

public class AddAccountDto {

	private final AddResult addResult;
	private final AccountDto accountDto;
	
	public AddAccountDto(AddResult addResult, AccountDto accountDto) {
		this.addResult = addResult;
		this.accountDto = accountDto;
	}

	public AddResult getAddResult() {
		return addResult;
	}

	public AccountDto getAccountDto() {
		return accountDto;
	}
}
