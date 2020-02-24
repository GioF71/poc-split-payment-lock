package eu.giof.poc.rest.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountSlotListDto {

	@NonNull
	@Getter
	private final String accountId;
	
	@Getter
	private Double balance;
	
	public static AccountSlotListDto create(String accountId) {
		return new AccountSlotListDto(accountId);
	}
	
	private final List<BalanceSlotDto> balanceSlotList = new ArrayList<>();
	
	public AccountSlotListDto add(BalanceSlotDto balanceSlotDto) {
		balance = Optional.ofNullable(balance).orElse(Double.valueOf(0.0f));
		balance += Optional.ofNullable(balanceSlotDto.getAmount()).orElse(Double.valueOf(0.0f));
		balanceSlotList.add(balanceSlotDto);
		return this;
	}
	
	public List<BalanceSlotDto> getBalanceSlotList() {
		return Collections.unmodifiableList(balanceSlotList);
	}
}
