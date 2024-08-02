package com.alibou.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.alibou.payment.paypal")
public class PaypalIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaypalIntegrationApplication.class, args);
	}

}
