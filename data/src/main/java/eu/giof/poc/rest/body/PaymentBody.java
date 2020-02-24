package eu.giof.poc.rest.body;

import lombok.Getter;
import lombok.Setter;

public class PaymentBody {

	@Getter
	@Setter
	private String payerAccountId;

	@Getter
	@Setter
	private String payeeAccountId;
	
	@Getter
	@Setter
	private Double amount;
	
	@Getter
	@Setter
	private Integer waitBeforeMoveSec;

	@Getter
	@Setter
	private Integer waitAfterMoveSec;
}
