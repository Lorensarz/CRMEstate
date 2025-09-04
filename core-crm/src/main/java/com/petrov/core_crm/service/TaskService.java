package com.petrov.core_crm.service;

import com.petrov.core_crm.dto.TaskRequest;
import com.petrov.core_crm.entity.Building;
import com.petrov.core_crm.entity.Task;
import com.petrov.core_crm.entity.User;
import com.petrov.core_crm.repository.BuildingRepository;
import com.petrov.core_crm.repository.TaskRepository;
import com.petrov.core_crm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

	private final TaskRepository taskRepository;
	private final UserRepository userRepository;
	private final BuildingRepository buildingRepository;

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

		return taskRepository.save(task);
	}

	@Transactional
	@Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 3)
	public Task update(Long taskId, TaskRequest request) {
		Task task = findById(taskId);

		// Optimistic lock проверка
		if (request.getVersion() != null && !task.getVersion().equals(request.getVersion())) {
			throw new ObjectOptimisticLockingFailureException(
					"Task was updated by another transaction", Task.class);
		}

		// Обновляем поля
		task.setTitle(request.getTitle());
		task.setDescription(request.getDescription());
		task.setStatus(request.getStatus());
		task.setPriority(request.getPriority());
		task.setDueDate(request.getDueDate());

		// Обновляем связи если нужно
		if (request.getAssignedToId() != null) {
			User user = getUserById(request.getAssignedToId());
			task.setAssignedTo(user);
		}

		if (request.getBuildingId() != null) {
			Building building = getBuildingById(request.getBuildingId());
			task.setBuilding(building);
		}

		return taskRepository.save(task);
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
