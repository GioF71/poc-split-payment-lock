package eu.giof.poc.service.structure;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {

	@NonNull
	@Getter
	private final String id;
	
	@NonNull
	@Getter
	private final String name;
	
	public static Account valueOf(String id, String name) {
		return new Account(id, name);
	}
}
