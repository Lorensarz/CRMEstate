package com.petrov.notification.service;

import com.petrov.notification.dto.NotificationRequest;
import com.petrov.notification.exception.NotificationFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {

	private final WebClient smsWebClient;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Value("${notification.external.sms.timeout}")
	private int timeout;

	@Value("${spring.kafka.topic.dlt:notifications.DLT}")
	private String dltTopic;

	@Retryable(
			retryFor = {NotificationFailedException.class, WebClientResponseException.class},
			maxAttemptsExpression = "${notification.retry.max-attempts:5}",
			backoff = @Backoff(
					delayExpression = "${notification.retry.backoff-delay:2000}",
					multiplierExpression = "${notification.retry.backoff-multiplier:2}"
			)
	)
	public void sendSms(NotificationRequest request) {
		log.info("Attempting to send SMS to: {}", request.getTo());

		try {
			String response = smsWebClient.post()
					.bodyValue(request)
					.retrieve()
					.bodyToMono(String.class)
					.timeout(Duration.ofMillis(timeout))
					.block();

			log.info("SMS sent successfully to: {}, response: {}", request.getTo(), response);

		} catch (WebClientResponseException e) {
			log.error("SMS API error for {}: {}", request.getTo(), e.getStatusCode());
			throw new NotificationFailedException("SMS API error: " + e.getStatusCode(), e);
		} catch (Exception e) {
			log.error("Failed to send SMS to {}: {}", request.getTo(), e.getMessage());
			throw new NotificationFailedException("SMS sending failed: " + e.getMessage(), e);
		}
	}

	@Recover
	public void recoverSms(NotificationFailedException e, NotificationRequest request) {
		log.error("All retry attempts failed for SMS to: {}. Error: {}", request.getTo(), e.getMessage());
		sendToDlq(request, "SMS", e.getMessage());
	}

	private void sendToDlq(NotificationRequest request, String type, String error) {
		Map<String, Object> dlqMessage = new HashMap<>();
		dlqMessage.put("originalRequest", request);
		dlqMessage.put("error", error);
		dlqMessage.put("type", type);
		dlqMessage.put("timestamp", Instant.now());
		dlqMessage.put("retryAttempts", 5);

		try {
			kafkaTemplate.send(dltTopic, dlqMessage);
			log.warn("Sent to DLQ: {} notification failed for {}", type, request.getTo());
		} catch (Exception kafkaException) {
			log.error("Failed to send message to DLQ for {}: {}", request.getTo(), kafkaException.getMessage());
		}
	}
}