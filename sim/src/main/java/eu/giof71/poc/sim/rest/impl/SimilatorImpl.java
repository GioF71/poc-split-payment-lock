package eu.giof71.poc.sim.rest.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import eu.giof.poc.rest.body.AddAccount;
import eu.giof.poc.rest.dto.AddAccountDto;
import eu.giof71.poc.sim.rest.Simulator;
import eu.giof71.poc.sim.rest.body.TestBed;
import eu.giof71.poc.sim.rest.dto.PrepareTestBedResult;
import eu.giof71.poc.sim.rest.dto.Result;
import eu.giof71.poc.sim.rest.dto.TestBedAccount;
import eu.giof71.poc.sim.service.SimulationData;

@RestController
public class SimilatorImpl implements Simulator {
	
	@Autowired
	private SimulationData simulationData;

	@Override
	@PutMapping(value = "/simulator/prepare")
	public PrepareTestBedResult prepare(@RequestBody TestBed testBed) {
		PrepareTestBedResult result = new PrepareTestBedResult();
		
	    final String uri = "http://localhost:8080/account/add";
	    
	    AddAccount addAccount = new AddAccount();
	    addAccount.setId(UUID.randomUUID().toString());
	    addAccount.setBalance(testBed.getBalance());
	    addAccount.setName("XXX");
	    addAccount.setSlotCount(testBed.getSlotCount());
	    
	 
	    RestTemplate restTemplate = new RestTemplate();
	    AddAccountDto addAccountResult = restTemplate.postForObject(uri, addAccount, AddAccountDto.class);
	 
	    System.out.println(addAccountResult);
	    
	    result.setResult(Result.OK);
	    result.add(convert(addAccountResult));
	    
	    return result;
	    
	}
	
	private TestBedAccount convert(AddAccountDto dto) {
		TestBedAccount c = new TestBedAccount();
		c.setAccountId(dto.getAccountDto().getId());
		c.setAccountName(dto.getAccountDto().getName());
		c.setBalance(dto.getAccountDto().getBalance());
		c.setSlotCount(dto.getAccountDto().getSlotCount());
		return c;
	}

}
