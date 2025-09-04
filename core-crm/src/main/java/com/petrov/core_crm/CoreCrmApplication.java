package com.petrov.core_crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableRetry
@EnableAsync
@SpringBootApplication
public class CoreCrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreCrmApplication.class, args);
	}

}
