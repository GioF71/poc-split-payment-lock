package eu.giof71.poc.sim.rest;

import eu.giof71.poc.sim.rest.body.TestBed;
import eu.giof71.poc.sim.rest.dto.PrepareTestBedResult;

public interface Simulator {
	PrepareTestBedResult prepare(TestBed testBed);
}
