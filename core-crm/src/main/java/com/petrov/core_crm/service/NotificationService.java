package com.petrov.core_crm.service;

import com.petrov.core_crm.entity.Task;
import com.petrov.core_crm.entity.User;
import com.petrov.core_crm.enums.TaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Async
	public void sendTaskCreatedNotification(Task task) {
		TaskNotificationEvent event = new TaskNotificationEvent(
				task.getId(),
				task.getTitle(),
				"TASK_CREATED",
				task.getAssignedTo().getUsername(),
				task.getDueDate()
		);
		kafkaTemplate.send("crm-task-events", event);
		log.info("Sent task created notification: {}", task.getId());
	}

	@Async
	public void sendTaskStatusChangedNotification(Task task, TaskStatus oldStatus) {
		TaskNotificationEvent event = new TaskNotificationEvent(
				task.getId(),
				task.getTitle(),
				"STATUS_CHANGED",
				task.getAssignedTo().getUsername(),
				task.getDueDate(),
				"Status changed from " + oldStatus + " to " + task.getStatus()
		);
		kafkaTemplate.send("crm-task-events", event);
		log.info("Sent task status changed notification: {}", task.getId());
	}

	@Async
	public void sendTaskReassignedNotification(Task task, User previousAssignee) {
		TaskNotificationEvent event = new TaskNotificationEvent(
				task.getId(),
				task.getTitle(),
				"REASSIGNED",
				task.getAssignedTo().getUsername(),
				task.getDueDate(),
				"Reassigned from " + previousAssignee.getUsername() + " to " + task.getAssignedTo().getUsername()
		);
		kafkaTemplate.send("crm-task-events", event);
		log.info("Sent task reassigned notification: {}", task.getId());
	}

	@Async
	public void sendAutoTaskCreatedNotification(Task task) {
		TaskNotificationEvent event = new TaskNotificationEvent(
				task.getId(),
				task.getTitle(),
				"AUTO_TASK_CREATED",
				task.getAssignedTo().getUsername(),
				task.getDueDate(),
				"Automatically created for inactive building"
		);
		kafkaTemplate.send("crm-task-events", event);
		log.info("Sent auto task created notification: {}", task.getId());
	}

	public record TaskNotificationEvent(
			Long taskId,
			String title,
			String eventType,
			String assignedTo,
			LocalDateTime dueDate,
			String additionalInfo
	) {
		public TaskNotificationEvent(Long taskId, String title, String eventType,
									 String assignedTo, LocalDateTime dueDate) {
			this(taskId, title, eventType, assignedTo, dueDate, null);
		}
	}

}
