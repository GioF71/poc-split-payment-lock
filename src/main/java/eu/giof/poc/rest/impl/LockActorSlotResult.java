package eu.giof.poc.rest.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.Getter;

class LockActorSlotResult {
	
	@Getter
	private final boolean success;
	
	private final List<ActorSlot> actorSlotList;
	
	static LockActorSlotResult valueOf(boolean success, List<ActorSlot> selectedList) {
		return new LockActorSlotResult(success, selectedList);
	}
	
	private LockActorSlotResult(boolean success, List<ActorSlot> actorSlotList) {
		this.success = success;
		this.actorSlotList = actorSlotList;
	}
	
	List<ActorSlot> getActorSlotList() {
		return Collections.unmodifiableList(
			Optional.ofNullable(actorSlotList).orElse(Collections.emptyList()));
	}
}
