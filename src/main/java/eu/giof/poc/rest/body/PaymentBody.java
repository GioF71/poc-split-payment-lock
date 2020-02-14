package eu.giof.poc.rest.body;

public class PaymentBody {

	private String payerAccountId;
	private String payeeAccountId;
	private Double amount;
	private Integer waitSec;

	public String getPayerAccountId() {
		return payerAccountId;
	}

	public void setPayerAccountId(String payerAccountId) {
		this.payerAccountId = payerAccountId;
	}

	public String getPayeeAccountId() {
		return payeeAccountId;
	}

	public void setPayeeAccountId(String payeeAccountId) {
		this.payeeAccountId = payeeAccountId;
	}

	public Double getAmount() {
		return amount;
	}

	public Integer getWaitSec() {
		return waitSec;
	}

	public void setWaitSec(Integer waitSec) {
		this.waitSec = waitSec;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
