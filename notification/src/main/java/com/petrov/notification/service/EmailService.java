package com.petrov.notification.service;

import com.petrov.notification.dto.NotificationRequest;
import com.petrov.notification.exception.NotificationFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

	private final WebClient emailWebClient;

	@Value("${notification.external.email.timeout:5000}")
	private int timeout;

	@Retryable(
			retryFor = {NotificationFailedException.class, WebClientResponseException.class},
			maxAttemptsExpression = "${notification.retry.max-attempts:5}",
			backoff = @Backoff(
					delayExpression = "${notification.retry.backoff-delay:2000}",
					multiplierExpression = "${notification.retry.backoff-multiplier:2}"
			)
	)
	public void sendEmail(NotificationRequest request) {
		log.info("Attempting to send Email to: {}", request.getTo());

		try {
			String response = emailWebClient.post()
					.bodyValue(request)
					.retrieve()
					.bodyToMono(String.class)
					.timeout(Duration.ofMillis(timeout))
					.block();

			log.info("Email sent successfully to: {}, response: {}", request.getTo(), response);

		} catch (WebClientResponseException e) {
			log.error("Email API error for {}: {}", request.getTo(), e.getStatusCode());
			throw new NotificationFailedException("Email API error: " + e.getStatusCode(), e);
		} catch (Exception e) {
			log.error("Failed to send Email to {}: {}", request.getTo(), e.getMessage());
			throw new NotificationFailedException("Email sending failed: " + e.getMessage(), e);
		}
	}

	@Recover
	public void recoverEmail(NotificationFailedException e, NotificationRequest request) {
		log.error("All retry attempts failed for Email to: {}. Error: {}", request.getTo(), e.getMessage());
		sendToDlq(request, "Email", e.getMessage());
	}

	private void sendToDlq(NotificationRequest request, String type, String error) {
		log.warn("Sending to DLQ: {} notification failed for {} - Error: {}",
				type, request.getTo(), error);
	}
}
