package eu.giof.poc.rest.body;

import lombok.Getter;
import lombok.Setter;

public class AddAccount {

	@Getter
	@Setter
	private String id;

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private Double balance;

	@Getter
	@Setter
	private Integer slotCount;
}
