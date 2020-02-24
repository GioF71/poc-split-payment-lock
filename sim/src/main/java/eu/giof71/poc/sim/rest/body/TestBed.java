package eu.giof71.poc.sim.rest.body;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TestBed {
	
	@Getter
	private final int numAccount;
	
	@Getter
	private final double balance;
	
	@Getter
	private final int slotCount;
	
	public static TestBed valueOf(int numAccount, double balance, int slotCount) {
		return new TestBed(numAccount, balance, slotCount);
	}
}

