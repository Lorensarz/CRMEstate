package com.petrov.core_crm.service;

import com.petrov.core_crm.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Async
	public void sendTaskCreatedNotification(Task task) {
		TaskNotificationEvent event = new TaskNotificationEvent(
				task.getId(),
				task.getTitle(),
				task.getAssignedTo().getUsername(),
				task.getDueDate()
		);
		kafkaTemplate.send("task-created", event);
	}

	public record TaskNotificationEvent(
			Long taskId,
			String title,
			String assignedTo,
			LocalDateTime dueDate
	) {}
}
