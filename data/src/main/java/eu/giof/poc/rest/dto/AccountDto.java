package eu.giof.poc.rest.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class AccountDto {

	@NonNull
	@Getter
	@Setter
	private String id;
	
	@NonNull
	@Getter
	@Setter
	private String name;

	@NonNull
	@Getter
	@Setter
	private Double balance;	
	
	@NonNull
	@Getter
	@Setter
	private Integer slotCount;
	
	public static AccountDto valueOf(String id, String name, Double balance, Integer slotCount) {
		return new AccountDto(id, name, balance, slotCount);
	}
}
