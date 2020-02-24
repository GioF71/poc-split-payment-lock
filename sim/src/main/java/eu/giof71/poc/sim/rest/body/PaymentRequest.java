package eu.giof71.poc.sim.rest.body;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
	private String payer;
	private String payee;
	private Double amount;
}
