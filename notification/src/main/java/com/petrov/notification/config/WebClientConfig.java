package com.petrov.notification.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Value("${notification.external.sms.timeout}")
	private int smsTimeout;

	@Value("${notification.external.email.timeout}")
	private int emailTimeout;

	@Bean
	public WebClient smsWebClient(@Value("${notification.external.sms.url}") String smsUrl) {
		return WebClient.builder()
				.baseUrl(smsUrl)
				.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
				.build();
	}

	@Bean
	public WebClient emailWebClient(@Value("${notification.external.email.url}") String emailUrl) {
		return WebClient.builder()
				.baseUrl(emailUrl)
				.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
				.build();
	}
}
