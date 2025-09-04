package com.petrov.core_crm.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskResponse {
	private Long id;
	private String title;
	private String description;
	private String status;
	private String priority;
	private LocalDateTime dueDate;
	private Long assignedToId;
	private String assignedToName;
	private Long buildingId;
	private String buildingAddress;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Integer version;
}
