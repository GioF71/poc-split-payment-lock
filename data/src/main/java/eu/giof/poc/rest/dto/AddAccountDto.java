package eu.giof.poc.rest.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class AddAccountDto {

	@NonNull
	@Getter
	@Setter
	private AddResult addResult;

	@Getter
	@Setter
	private AccountDto accountDto;
	
	public static AddAccountDto alreadyExists() {
		return new AddAccountDto(AddResult.ALREADY_EXISTS);
	}

	public static AddAccountDto valueOf(AddResult addResult, AccountDto accountDto) {
		AddAccountDto dto = new AddAccountDto(addResult);
		dto.setAccountDto(accountDto);
		return dto;
	}
}
