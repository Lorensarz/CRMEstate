package com.petrov.notification.dto;

import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class DlqMessage {
	private NotificationRequest originalRequest;
	private String error;
	private String type;
	private Instant timestamp;
	private int retryAttempts;
	private Map<String, Object> additionalInfo;
}