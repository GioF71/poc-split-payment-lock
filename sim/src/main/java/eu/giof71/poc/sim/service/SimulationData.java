package eu.giof71.poc.sim.service;

public interface SimulationData {
	boolean accountExists(String accountId);
	boolean createAccount(String accountId, double initialBalance);
	double getBalance(String accountId);
	void addPaymentInstruction(PaymentInstruction instruction);
	PaymentInstruction popPaymentInstruction();
	int getPendingPaymentInstructionCount();
}
