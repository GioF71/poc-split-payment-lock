package eu.giof.poc.rest.dto;

import lombok.Getter;

@Getter
public class AccountDto {
	
	private final String id;
	private final String name;

	public AccountDto(String id, String name) {
		this.id = id;
		this.name = name;
	}
}
