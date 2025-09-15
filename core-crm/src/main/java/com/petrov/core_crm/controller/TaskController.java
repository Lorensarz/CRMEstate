package com.petrov.core_crm.controller;

import com.petrov.core_crm.dto.TaskRequest;
import com.petrov.core_crm.dto.TaskResponse;
import com.petrov.core_crm.entity.Task;
import com.petrov.core_crm.enums.TaskPriority;
import com.petrov.core_crm.enums.TaskStatus;
import com.petrov.core_crm.mapper.TaskMapper;
import com.petrov.core_crm.service.TaskService;
import com.petrov.core_crm.specification.TaskSpecifications;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

	private final TaskService taskService;
	private final TaskMapper taskMapper;

	@GetMapping
	public ResponseEntity<Page<TaskResponse>> getTasks(
			@RequestParam(required = false) Long assignedTo,
			@RequestParam(required = false) Long buildingId,
			@RequestParam(required = false) TaskStatus status,
			@RequestParam(required = false) TaskPriority priority,
			@RequestParam(required = false) LocalDateTime dueDateFrom,
			@RequestParam(required = false) LocalDateTime dueDateTo,
			Pageable pageable) {

		Specification<Task> spec = TaskSpecifications.withFilters(
				assignedTo, buildingId, status, priority, dueDateFrom, dueDateTo);
		Page<Task> tasks = taskService.findAll(spec, pageable);
		Page<TaskResponse> response = tasks.map(taskMapper::toResponse);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TaskResponse> getTask(@PathVariable Long id) {
		Task task = taskService.findById(id);
		TaskResponse response = taskMapper.toResponse(task);
		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
		Task task = taskMapper.toEntity(request);
		Task createdTask = taskService.create(task, request.getAssignedToId(), request.getBuildingId());
		TaskResponse response = taskMapper.toResponse(createdTask);
		return ResponseEntity.created(URI.create("/api/tasks/" + createdTask.getId()))
				.body(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<TaskResponse> updateTask(
			@PathVariable Long id,
			@Valid @RequestBody TaskRequest request) {

		Task updatedTask = taskService.update(id, request);
		TaskResponse response = taskMapper.toResponse(updatedTask);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
		taskService.delete(id);
		return ResponseEntity.noContent().build();
	}
}