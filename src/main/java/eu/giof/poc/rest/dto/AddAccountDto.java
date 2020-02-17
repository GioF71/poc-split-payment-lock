package eu.giof.poc.rest.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AddAccountDto {

	@NonNull
	@Getter
	private final AddResult addResult;

	@NonNull
	@Getter
	private final AccountDto accountDto;
	
	public static AddAccountDto valueOf(AddResult addResult, AccountDto accountDto) {
		return new AddAccountDto(addResult, accountDto);
	}
}
