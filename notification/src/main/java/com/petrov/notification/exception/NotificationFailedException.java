package com.petrov.notification.exception;

public class NotificationFailedException extends RuntimeException {

	public NotificationFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotificationFailedException(String message) {
		super(message);
	}
}