package com.petrov.core_crm.repository;

import com.petrov.core_crm.entity.Task;
import com.petrov.core_crm.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

	List<Task> findByAssignedToId(Long userId);

	List<Task> findByBuildingId(Long buildingId);

	List<Task> findByStatus(TaskStatus status);
}
