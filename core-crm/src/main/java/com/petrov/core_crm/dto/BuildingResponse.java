package com.petrov.core_crm.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BuildingResponse {
	private Long id;
	private String cadastrNumber;
	private String address;
	private String buildingType;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
