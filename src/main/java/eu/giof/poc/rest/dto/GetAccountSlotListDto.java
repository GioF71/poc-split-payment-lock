package eu.giof.poc.rest.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GetAccountSlotListDto {
	
	@Getter
	@NonNull
	private final String accountId;
	
	@Getter
	@NonNull
	private GetResult getResult;
	
	@Getter
	private AccountSlotListDto accountSlotListDto;
	
	public static GetAccountSlotListDto found(String accountId, AccountSlotListDto accountSlotListDto) {
		GetAccountSlotListDto dto = new GetAccountSlotListDto(accountId, GetResult.FOUND);
		dto.accountSlotListDto = accountSlotListDto;
		return dto;
	}
	
	public static GetAccountSlotListDto notFound(String accountId) {
		return new GetAccountSlotListDto(accountId, GetResult.NOT_FOUND);
		
	}
}
