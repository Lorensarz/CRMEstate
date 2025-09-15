package com.petrov.content_processor.controller;

import com.petrov.content_processor.service.QueueManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/monitoring")
@RequiredArgsConstructor
@Tag(name = "Monitoring", description = "Мониторинг и управление обработкой данных")
public class MonitoringController {

	private final QueueManagementService queueManagementService;

	@GetMapping("/queue-size")
	@Operation(summary = "Получить размер очереди обработки")
	public int getQueueSize() {
		return queueManagementService.getQueueSize();
	}

	@GetMapping("/health")
	@Operation(summary = "Проверить статус сервиса")
	public String healthCheck() {
		return "Service is running. Queue size: " + queueManagementService.getQueueSize();
	}
}