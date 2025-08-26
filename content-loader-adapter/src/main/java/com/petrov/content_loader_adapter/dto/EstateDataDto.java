package com.petrov.content_loader_adapter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EstateDataDto {

	@NotBlank(message = "Cadastr number is required")
	private String cadastrNumber;

	@NotBlank(message = "Source is required")
	private String source;

	@NotNull
	@Positive(message = "Price must be positive")
	private BigDecimal price;

	private String type;
	private BigDecimal square;
	private Integer roomCount;
	private Integer floor;
	private Integer totalFloors;
	private String address;
	private LocalDateTime updatedAt;


}
