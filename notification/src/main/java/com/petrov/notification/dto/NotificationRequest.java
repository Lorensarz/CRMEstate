package com.petrov.notification.dto;

import lombok.Data;

@Data
public class NotificationRequest {
	private String to;
	private String subject;
	private String message;
	private String type;
}
