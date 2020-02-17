package eu.giof.poc.rest.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountSlotListDto {

	@Getter
	@NonNull
	private final String accountId;
	
	@Getter
	private Double balance;
	
	private final List<BalanceSlotDto> balanceSlotList = new ArrayList<>();
	
	public void add(BalanceSlotDto balanceSlotDto) {
		balance = Optional.ofNullable(balance).orElse(Double.valueOf(0.0f));
		balance += Optional.ofNullable(balanceSlotDto.getAmount()).orElse(Double.valueOf(0.0f));
		balanceSlotList.add(balanceSlotDto);
	}
	
	public List<BalanceSlotDto> getBalanceSlotList() {
		return Collections.unmodifiableList(balanceSlotList);
	}
}
