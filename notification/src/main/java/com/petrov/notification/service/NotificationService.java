package com.petrov.notification.service;

import com.petrov.notification.dto.NotificationRequest;
import com.petrov.notification.dto.TaskNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

	private final SmsService smsService;
	private final EmailService emailService;

	public void processNotification(TaskNotificationEvent event) {
		NotificationRequest request = createNotificationRequest(event);

		if ("SMS".equalsIgnoreCase(request.getType())) {
			smsService.sendSms(request);
		} else if ("EMAIL".equalsIgnoreCase(request.getType())) {
			emailService.sendEmail(request);
		} else {
			log.warn("Unknown notification type: {}", request.getType());
		}
	}

	private NotificationRequest createNotificationRequest(TaskNotificationEvent event) {
		NotificationRequest request = new NotificationRequest();
		request.setTo(event.getAssignedTo());
		request.setSubject("Task Notification: " + event.getTitle());
		request.setMessage(buildMessage(event));
		request.setType(determineNotificationType(event.getAssignedTo()));

		return request;
	}

	private String buildMessage(TaskNotificationEvent event) {
		return String.format(
				"Task: %s\nEvent: %s\nDue Date: %s\nAdditional Info: %s",
				event.getTitle(),
				event.getEventType(),
				event.getDueDate(),
				event.getAdditionalInfo() != null ? event.getAdditionalInfo() : "None"
		);
	}

	private String determineNotificationType(String contact) {
		return contact.contains("@") ? "EMAIL" : "SMS";
	}
}
