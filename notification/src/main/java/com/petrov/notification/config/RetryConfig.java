package com.petrov.notification.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@EnableRetry
@Configuration
public class RetryConfig {

	@Value("${notification.retry.max-attempts}")
	private int maxAttempts;

	@Value("${notification.retry.backoff-delay}")
	private long backoffDelay;

	@Value("${notification.retry.backoff-multiplier}")
	private double backoffMultiplier;

	@Bean
	public RetryTemplate retryTemplate() {
		RetryTemplate retryTemplate = new RetryTemplate();

		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(maxAttempts);
		retryTemplate.setRetryPolicy(retryPolicy);

		ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
		backOffPolicy.setInitialInterval(backoffDelay);
		backOffPolicy.setMultiplier(backoffMultiplier);
		retryTemplate.setBackOffPolicy(backOffPolicy);

		return retryTemplate;
	}
}
