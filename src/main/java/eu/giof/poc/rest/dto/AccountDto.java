package eu.giof.poc.rest.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountDto {

	@NonNull
	@Getter
	private final String id;
	
	@NonNull
	@Getter
	private final String name;

	public static AccountDto valueOf(String id, String name) {
		return new AccountDto(id, name);
	}
}
