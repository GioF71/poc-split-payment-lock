package eu.giof.poc.rest.dto;

import lombok.Getter;

@Getter
public class AddAccountDto {

	private final AddResult addResult;
	private final AccountDto accountDto;
	
	public AddAccountDto(AddResult addResult, AccountDto accountDto) {
		this.addResult = addResult;
		this.accountDto = accountDto;
	}
}
