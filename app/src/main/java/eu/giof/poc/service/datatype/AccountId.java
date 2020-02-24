package eu.giof.poc.service.datatype;

import eu.giof.poc.util.impl.WrapperComparatorProvider;

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
		return WrapperComparatorProvider.comparator(AccountId::get).compare(this, o);
	}

	public String get() {
		return value;
	}
}
