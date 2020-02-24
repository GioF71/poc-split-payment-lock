package eu.giof.poc.rest.impl.payment;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class LockActorSlotResult {
	
	@Getter
	private final boolean success;
	
	private final List<ActorSlot> actorSlotList;
	
	static LockActorSlotResult valueOf(boolean success, List<ActorSlot> selectedList) {
		return new LockActorSlotResult(success, selectedList);
	}
	
	List<ActorSlot> getActorSlotList() {
		return Collections.unmodifiableList(
			Optional.ofNullable(actorSlotList).orElse(Collections.emptyList()));
	}
}
