package eu.giof71.poc.sim.service.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import eu.giof71.poc.sim.service.PaymentInstruction;
import eu.giof71.poc.sim.service.SimulationData;

@Component
public class SimulationDataImpl implements SimulationData {
	
	private final Map<String, Double> accountBalance = new HashMap<>();
	private final List<PaymentInstruction> paymentInstructionList = new LinkedList<>();

	@Override
	public synchronized boolean accountExists(String accountId) {
		return accountBalance.containsKey(accountId);
	}

	@Override
	public synchronized boolean createAccount(String accountId, double initialBalance) {
		if (!accountBalance.containsKey(accountId)) {
			accountBalance.put(accountId, initialBalance);
			return true;
		} else {
			return false;
		}
	} 

	@Override
	public synchronized double getBalance(String accountId) {
		return accountBalance.get(accountId);
	}

	@Override
	public synchronized void addPaymentInstruction(PaymentInstruction instruction) {
		paymentInstructionList.add(instruction);
	}

	@Override
	public synchronized int getPendingPaymentInstructionCount() {
		return paymentInstructionList.size();
	}

	@Override
	public synchronized PaymentInstruction popPaymentInstruction() {
		return Optional.of(paymentInstructionList)
			.filter(l -> l.size() > 0)
			.map(l -> l.remove(0))
			.orElse(null);
	}
}
