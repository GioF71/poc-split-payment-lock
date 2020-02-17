package eu.giof.poc.rest.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BalanceSlotDto {
	
	@NonNull
	@Getter
	private Integer slotId;

	@NonNull
	@Getter
	private Double amount;
	
	public static BalanceSlotDto valueOf(Integer slotId, Double amount) {
		return new BalanceSlotDto(slotId, amount);
	}
}
