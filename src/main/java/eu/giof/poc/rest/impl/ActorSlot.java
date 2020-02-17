package eu.giof.poc.rest.impl;

import eu.giof.poc.service.structure.BalanceSlot;
import eu.giof.poc.service.structure.BalanceSlotKey;

class ActorSlot {
	
	private final BalanceSlot balanceSlot;
	private final BalanceSlotKey balanceSlotKey;
	
	static ActorSlot valueOf(BalanceSlot balanceSlot, BalanceSlotKey balanceSlotKey) {
		return new ActorSlot(balanceSlot, balanceSlotKey);
	}
	
	private ActorSlot(BalanceSlot balanceSlot, BalanceSlotKey balanceSlotKey) {
		this.balanceSlot = balanceSlot;
		this.balanceSlotKey = balanceSlotKey;
	}
	
	BalanceSlot getBalanceSlot() {
		return balanceSlot;
	}
	BalanceSlotKey getBalanceSlotKey() {
		return balanceSlotKey;
	}
}
