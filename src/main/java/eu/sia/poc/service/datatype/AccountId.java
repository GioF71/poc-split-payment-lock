package eu.sia.poc.service.datatype;

public class AccountId implements Comparable<AccountId> {

	private final String value;
	
	public static AccountId valueOf(String v) {
		return new AccountId(v);
	}
	
	private AccountId(String value) {
		this.value = value;
	}

	@Override
	public int compareTo(AccountId o) {
		return value.compareTo(o.value);
	}
}
