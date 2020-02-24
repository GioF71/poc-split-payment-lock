package eu.giof71.poc.sim.rest.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import eu.giof.poc.rest.body.AddAccount;
import eu.giof.poc.rest.dto.AddAccountDto;
import eu.giof.poc.rest.dto.AddResult;
import eu.giof71.poc.sim.rest.Simulator;
import eu.giof71.poc.sim.rest.body.PaymentRequest;
import eu.giof71.poc.sim.rest.body.TestBed;
import eu.giof71.poc.sim.rest.dto.PrepareTestBedResult;
import eu.giof71.poc.sim.rest.dto.Result;
import eu.giof71.poc.sim.rest.dto.TestBedAccount;
import eu.giof71.poc.sim.service.PaymentInstruction;
import eu.giof71.poc.sim.service.SimulationData;

@RestController
public class SimilatorImpl implements Simulator {
	
	@Autowired
	private SimulationData simulationData;

	@Override
	@PostMapping(value = "/simulator/prepare")
	public PrepareTestBedResult prepare(@RequestBody TestBed testBed) {
		PrepareTestBedResult result = new PrepareTestBedResult();
	    final String uri = "http://localhost:8080/account/add";
	    boolean allOk = true;
	    RestTemplate restTemplate = new RestTemplate();
	    for (int i = 0; i < testBed.getNumAccount(); ++i) {
		    AddAccount addAccount = new AddAccount();
		    addAccount.setId(UUID.randomUUID().toString());
		    addAccount.setBalance(testBed.getBalance());
		    addAccount.setName(String.format("Name for [%s]", addAccount.getId()));
		    addAccount.setSlotCount(testBed.getSlotCount());
		    AddAccountDto addAccountResult = restTemplate.postForObject(uri, addAccount, AddAccountDto.class);
		    if (AddResult.ADD_OK.equals(addAccountResult.getAddResult())) {
			    result.add(convert(addAccountResult));
			    simulationData.createAccount(addAccount.getId(), testBed.getBalance());
		    } else {
		    	allOk = false;
		    }
	    }
	    result.setResult(allOk ? Result.OK : Result.FAIL);
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

	@Override
	@PostMapping(value = "/simulator/addPaymentRequest")
	public PaymentInstruction addPaymentRequest(@RequestBody PaymentRequest request) {
		PaymentInstruction paymentInstruction = new PaymentInstruction();
		paymentInstruction.setAmount(request.getAmount());
		paymentInstruction.setPayee(request.getPayee());
		paymentInstruction.setPayer(request.getPayer());
		simulationData.addPaymentInstruction(paymentInstruction);
		return paymentInstruction;
	}

	@Override
	@GetMapping(value = "/simulator/pendingPaymentRequestCount")
	public int pendingPaymentRequestCount() {
		return simulationData.getPendingPaymentInstructionCount();
	}
}
