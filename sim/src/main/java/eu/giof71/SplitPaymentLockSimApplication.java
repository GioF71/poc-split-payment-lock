package eu.giof71;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SplitPaymentLockSimApplication {

	public static void main(String[] args) {
		SpringApplication.run(SplitPaymentLockSimApplication.class, args);
	}
}

