package eu.giof.poc.service.structure;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BalanceSlot {
	
	@NonNull
	@Getter
	@Setter
	private Double availableBalance;

	public static BalanceSlot valueOf(Double availableBalance) {
		return new BalanceSlot(availableBalance);
	}
}
