package com.petrov.notification.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

	@Value("${notification.external.sms.timeout}")
	private int smsTimeout;

	@Value("${notification.external.email.timeout}")
	private int emailTimeout;

	@Bean
	public WebClient smsWebClient(@Value("${notification.external.sms.url}") String smsUrl) {
		HttpClient httpClient = HttpClient.create()
				.responseTimeout(Duration.ofMillis(smsTimeout))
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, smsTimeout);

		return WebClient.builder()
				.baseUrl(smsUrl)
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
				.build();
	}

	@Bean
	public WebClient emailWebClient(@Value("${notification.external.email.url}") String emailUrl) {
		HttpClient httpClient = HttpClient.create()
				.responseTimeout(Duration.ofMillis(emailTimeout))
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, emailTimeout);

		return WebClient.builder()
				.baseUrl(emailUrl)
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
				.build();
	}
}