package com.petrov.core_crm.mapper;

import com.petrov.core_crm.dto.TaskRequest;
import com.petrov.core_crm.dto.TaskResponse;
import com.petrov.core_crm.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TaskMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "version", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "assignedTo", ignore = true)
	@Mapping(target = "building", ignore = true)
	Task toEntity(TaskRequest request);

	@Mapping(source = "assignedTo.id", target = "assignedToId")
	@Mapping(source = "assignedTo.firstName", target = "assignedToName")
	@Mapping(source = "building.id", target = "buildingId")
	@Mapping(source = "building.address", target = "buildingAddress")
	TaskResponse toResponse(Task task);
}
