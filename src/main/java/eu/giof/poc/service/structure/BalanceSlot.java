package eu.giof.poc.service.structure;

public class BalanceSlot {
	
	private Double availableBalance;

	public BalanceSlot(Double availableBalance) {
		this.availableBalance = availableBalance;
	}

	public Double getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(Double availableBalance) {
		this.availableBalance = availableBalance;
	}
}
