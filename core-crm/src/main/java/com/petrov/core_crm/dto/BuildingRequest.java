package com.petrov.core_crm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BuildingRequest {

	@NotBlank(message = "Cadastr number is required")
	private String cadastrNumber;

	@NotBlank(message = "Address is required")
	private String address;

	private String buildingType;
}
