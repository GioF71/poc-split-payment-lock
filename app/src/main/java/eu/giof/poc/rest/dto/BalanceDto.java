package eu.giof.poc.rest.dto;

import java.util.Optional;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BalanceDto {
	
	@Getter
	@NonNull
	private final String accountId;
	
	@Getter
	@NonNull
	private GetResult getResult;
	
	@Getter
	@Setter(value = AccessLevel.PRIVATE)
	private Double availableBalance;
	
	public static BalanceDto found(String accountId, Double availableBalance) {
		BalanceDto dto = new BalanceDto(accountId, GetResult.FOUND);
		dto.setAvailableBalance(Optional.ofNullable(availableBalance).orElse(Double.valueOf(0.0f)));
		return dto;
	}

	public static BalanceDto notFound(String accountId) {
		return new BalanceDto(accountId, GetResult.NOT_FOUND);
	}
}
