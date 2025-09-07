package com.petrov.notification.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskNotificationEvent {
	private Long taskId;
	private String title;
	private String eventType;
	private String assignedTo;
	private LocalDateTime dueDate;
	private String additionalInfo;
}
