package eu.giof71;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableScheduling
@SpringBootApplication
@EnableAsync
public class SplitPaymentLockSimApplication {

	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(4);
		//executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("PaymentSubmitter-");
		executor.initialize();
		return executor;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SplitPaymentLockSimApplication.class, args);
	}
}

