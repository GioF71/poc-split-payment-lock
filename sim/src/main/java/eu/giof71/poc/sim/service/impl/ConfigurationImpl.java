package eu.giof71.poc.sim.service.impl;

import org.springframework.stereotype.Component;

import eu.giof71.poc.sim.service.Configuration;

@Component
public class ConfigurationImpl implements Configuration {

	@Override
	public int getNumberOfThreads() {
		return 4;
	}
}
