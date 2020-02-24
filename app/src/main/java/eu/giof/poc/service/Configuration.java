package eu.giof.poc.service;

public interface Configuration {
	int getMinSlotCount();
	int getMaxSlotCount();
	int getDefaultSlotCount();
	int getOptimisticLockTryCount();
}
