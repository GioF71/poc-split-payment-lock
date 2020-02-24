package eu.giof71.poc.sim.service;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentInstruction {
	private final String id = UUID.randomUUID().toString();
	private String payer;
	private String payee;
	private Double amount;
}
