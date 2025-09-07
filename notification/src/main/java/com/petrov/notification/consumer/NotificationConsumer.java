package com.petrov.notification.consumer;

import com.petrov.notification.dto.TaskNotificationEvent;
import com.petrov.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {

	private final NotificationService notificationService;

	@KafkaListener(topics = "notifications", groupId = "notification-group")
	public void consumeNotification(@Payload TaskNotificationEvent event, Acknowledgment ack) {
		try {
			log.info("Received notification event: {}", event);

			notificationService.processNotification(event);

			ack.acknowledge();
			log.info("Notification processed successfully: {}", event.getTaskId());

		} catch (Exception e) {
			log.error("Failed to process notification: {}", event.getTaskId(), e);
			throw e;
		}
	}

	@KafkaListener(topics = "notifications.DLT", groupId = "notification-dlt-group")
	public void consumeDltMessage(@Payload TaskNotificationEvent event, Acknowledgment ack) {
		log.warn("Received message from Dead Letter Topic: {}", event);
		ack.acknowledge();
	}
}
