package com.petrov.content_processor.batch;

public class EstateProcessingException extends RuntimeException {
	public EstateProcessingException(String message, Throwable cause) {
		super(message, cause);
	}

	public EstateProcessingException(String message) {
		super(message);
	}
}