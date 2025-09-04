package com.petrov.core_crm.mapper;

import com.petrov.core_crm.dto.BuildingRequest;
import com.petrov.core_crm.dto.BuildingResponse;
import com.petrov.core_crm.entity.Building;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BuildingMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "tasks", ignore = true)
	Building toEntity(BuildingRequest request);

	BuildingResponse toResponse(Building building);
}
