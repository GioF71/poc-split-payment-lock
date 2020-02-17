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
	private Double balance = Double.valueOf(0.0f);

	@Getter
	@Setter
	private Integer slotCount = 1;
}
