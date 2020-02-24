package eu.giof71.poc.sim.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import eu.giof71.poc.sim.service.SimulationData;

@Component
public class SimulationDataImpl implements SimulationData {
	
	private final Map<String, Double> accountBalance = new HashMap<>();

	@Override
	public synchronized boolean accountExists(String accountId) {
		return accountBalance.containsKey(accountId);
	}

	@Override
	public synchronized boolean createAccount(String accountId, double balance) {
		if (!accountBalance.containsKey(accountId)) {
			accountBalance.put(accountId, balance);
			return true;
		} else {
			return false;
		}
	} 

	@Override
	public synchronized double getBalance(String accountId) {
		return accountBalance.get(accountId);
	}

}
