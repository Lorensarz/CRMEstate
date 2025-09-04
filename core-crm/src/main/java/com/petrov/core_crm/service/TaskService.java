package com.petrov.core_crm.service;

import com.petrov.core_crm.dto.TaskRequest;
import com.petrov.core_crm.entity.Building;
import com.petrov.core_crm.entity.Task;
import com.petrov.core_crm.entity.User;
import com.petrov.core_crm.enums.TaskStatus;
import com.petrov.core_crm.repository.BuildingRepository;
import com.petrov.core_crm.repository.TaskRepository;
import com.petrov.core_crm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

	private final TaskRepository taskRepository;
	private final UserRepository userRepository;
	private final BuildingRepository buildingRepository;
	private final NotificationService notificationService;

	public Page<Task> findAll(Pageable pageable) {
		return taskRepository.findAll(pageable);
	}

	public Task findById(Long id) {
		return taskRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
	}

	@Transactional
	public Task create(Task task, Long assignedToId, Long buildingId) {
		User user = getUserById(assignedToId);
		Building building = getBuildingById(buildingId);

		task.setAssignedTo(user);
		task.setBuilding(building);

		Task savedTask = taskRepository.save(task);

		try {
			notificationService.sendTaskCreatedNotification(savedTask);
		} catch (Exception e) {
			log.error("Failed to send notification for task: {}", savedTask.getId(), e);
		}

		return savedTask;
	}

	@Transactional
	@Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 3)
	public Task update(Long taskId, TaskRequest request) {
		Task task = findById(taskId);

		if (request.getVersion() != null && !task.getVersion().equals(request.getVersion())) {
			throw new ObjectOptimisticLockingFailureException(
					"Task was updated by another transaction", Task.class);
		}

		TaskStatus oldStatus = task.getStatus();
		User oldAssignedUser = task.getAssignedTo();

		task.setTitle(request.getTitle());
		task.setDescription(request.getDescription());
		task.setStatus(request.getStatus());
		task.setPriority(request.getPriority());
		task.setDueDate(request.getDueDate());

		if (request.getAssignedToId() != null) {
			User user = getUserById(request.getAssignedToId());
			task.setAssignedTo(user);
		}

		if (request.getBuildingId() != null) {
			Building building = getBuildingById(request.getBuildingId());
			task.setBuilding(building);
		}

		Task updatedTask = taskRepository.save(task);

		try {
			if (oldStatus != updatedTask.getStatus()) {
				notificationService.sendTaskStatusChangedNotification(updatedTask, oldStatus);
			}
			if (oldAssignedUser != null && !oldAssignedUser.equals(updatedTask.getAssignedTo())) {
				notificationService.sendTaskReassignedNotification(updatedTask, oldAssignedUser);
			}
		} catch (Exception e) {
			log.error("Failed to send notification for task update: {}", updatedTask.getId(), e);
		}

		return updatedTask;
	}

	@Transactional
	public void delete(Long id) {
		Task task = findById(id);
		taskRepository.delete(task);
	}

	private User getUserById(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
	}

	private Building getBuildingById(Long buildingId) {
		return buildingRepository.findById(buildingId)
				.orElseThrow(() -> new RuntimeException("Building not found with id: " + buildingId));
	}
}
