package eu.sia.poc.rest.body;

public class AddAccount {

	private String id;
	private String name;
	private Double balance = Double.valueOf(0.0f);
	private Integer slotCount = 1;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Integer getSlotCount() {
		return slotCount;
	}

	public void setSlotCount(Integer slotCount) {
		this.slotCount = slotCount;
	}
}
