package com.petrov.core_crm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
		log.error("Runtime exception: {}", ex.getMessage(), ex);
		ErrorResponse error = new ErrorResponse(
				HttpStatus.NOT_FOUND.value(),
				ex.getMessage(),
				LocalDateTime.now()
		);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(ObjectOptimisticLockingFailureException.class)
	public ResponseEntity<ErrorResponse> handleOptimisticLockException(ObjectOptimisticLockingFailureException ex) {
		log.warn("Optimistic lock exception: {}", ex.getMessage());
		ErrorResponse error = new ErrorResponse(
				HttpStatus.CONFLICT.value(),
				"Объект был изменен другим пользователем. Пожалуйста, обновите страницу и попробуйте снова.",
				LocalDateTime.now()
		);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error ->
				errors.put(error.getField(), error.getDefaultMessage()));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
		log.error("Unexpected error: {}", ex.getMessage(), ex);
		ErrorResponse error = new ErrorResponse(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Внутренняя ошибка сервера",
				LocalDateTime.now()
		);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	public record ErrorResponse(int status, String message, LocalDateTime timestamp) {}
}