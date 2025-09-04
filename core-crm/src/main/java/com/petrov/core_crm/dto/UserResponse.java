package com.petrov.core_crm.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
	private Long id;
	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
