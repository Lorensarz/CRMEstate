package com.petrov.core_crm.dto;

import com.petrov.core_crm.enums.TaskPriority;
import com.petrov.core_crm.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequest {
	@NotBlank(message = "Title is required")
	private String title;

	private String description;

	@NotNull(message = "Assigned user is required")
	private Long assignedToId;

	@NotNull(message = "Building is required")
	private Long buildingId;

	private LocalDateTime dueDate;
	private TaskStatus status;
	private TaskPriority priority;
	private Integer version;
}
