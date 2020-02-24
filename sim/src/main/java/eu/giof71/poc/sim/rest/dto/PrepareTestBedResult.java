package eu.giof71.poc.sim.rest.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@NoArgsConstructor
public class PrepareTestBedResult {
	
	@Getter
	@Setter
	@NonNull
	private Result result;
	
	private final List<TestBedAccount> accountList = new ArrayList<>();

	public void add(TestBedAccount account) {
		accountList.add(account);
	}
	
	public List<TestBedAccount> getAccountList() {
		return Collections.unmodifiableList(accountList);
	}
}
