package eu.giof71.poc.sim.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TestBedAccount {
	
	private String accountId;
	private String accountName;
	private double balance;
	private int slotCount;
	
}
