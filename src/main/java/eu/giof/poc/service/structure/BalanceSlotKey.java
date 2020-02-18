package eu.giof.poc.service.structure;

import com.google.common.collect.ComparisonChain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BalanceSlotKey implements Comparable<BalanceSlotKey> {

	@NonNull
	@Getter
	private final String accountId;

	@NonNull
	@Getter
	private final Integer slotId;
	
	public static BalanceSlotKey valueOf(String accountId, Integer slotId) {
		return new BalanceSlotKey(accountId, slotId);
	}
	
	@Override
	public int compareTo(BalanceSlotKey o) {
		return ComparisonChain.start()
			.compare(accountId, o.accountId)
			.compare(slotId, o.slotId)
			.result();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
		result = prime * result + ((slotId == null) ? 0 : slotId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BalanceSlotKey other = (BalanceSlotKey) obj;
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		if (slotId == null) {
			if (other.slotId != null)
				return false;
		} else if (!slotId.equals(other.slotId))
			return false;
		return true;
	}
}
