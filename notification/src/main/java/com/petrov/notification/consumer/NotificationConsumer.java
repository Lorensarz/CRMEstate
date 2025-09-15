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

	@KafkaListener(
			topics = "${spring.kafka.topic.main:notifications}",
			groupId = "${spring.kafka.consumer.group-id:notification-group}",
			containerFactory = "kafkaListenerContainerFactory" // Убедитесь, что это имя совпадает с бин-методом
	)
	public void consumeNotification(@Payload TaskNotificationEvent event, Acknowledgment ack) {
		try {
			log.info("Received notification event for task: {}", event.getTaskId());

			notificationService.processNotification(event);

			ack.acknowledge();
			log.info("Notification processed successfully: {}", event.getTaskId());

		} catch (Exception e) {
			log.error("Failed to process notification for task: {}", event.getTaskId(), e);
			throw e; // Будет обработано DefaultErrorHandler и отправлено в DLT
		}
	}

	@KafkaListener(
			topics = "${spring.kafka.topic.dlt:notifications.DLT}",
			groupId = "notification-dlt-group",
			containerFactory = "kafkaListenerContainerFactory"
	)
	public void consumeDltMessage(@Payload Object dlqMessage, Acknowledgment ack) {
		try {
			log.warn("Received message from Dead Letter Topic: {}", dlqMessage);
			// Здесь можно добавить логику обработки DLT сообщений

		} catch (Exception e) {
			log.error("Error processing DLT message: {}", dlqMessage, e);
		} finally {
			ack.acknowledge();
		}
	}
}