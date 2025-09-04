package com.petrov.core_crm.mapper;

import com.petrov.core_crm.dto.UserRequest;
import com.petrov.core_crm.dto.UserResponse;
import com.petrov.core_crm.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "tasks", ignore = true)
	User toEntity(UserRequest request);

	UserResponse toResponse(User user);
}
