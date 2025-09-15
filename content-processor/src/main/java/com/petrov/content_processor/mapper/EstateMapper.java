package com.petrov.content_processor.mapper;

import com.petrov.content_processor.dto.EstateDataDto;
import com.petrov.content_processor.entity.Estate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface EstateMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "version", ignore = true)
	Estate toEntity(EstateDataDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "version", ignore = true)
	void updateEntityFromDto(EstateDataDto dto, @MappingTarget Estate entity);

}
