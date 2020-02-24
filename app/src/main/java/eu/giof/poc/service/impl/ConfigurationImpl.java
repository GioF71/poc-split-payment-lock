package eu.giof.poc.service.impl;

import org.springframework.stereotype.Component;

import eu.giof.poc.service.Configuration;

@Component
public class ConfigurationImpl implements Configuration {

	@Override
	public int getDefaultSlotCount() {
		return 10;
	}

	@Override
	public int getMinSlotCount() {
		return 5;
	}

	@Override
	public int getMaxSlotCount() {
		return 50;
	}

	@Override
	public int getOptimisticLockTryCount() {
		return 2;
	}
}
