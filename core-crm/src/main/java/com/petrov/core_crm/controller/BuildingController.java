package com.petrov.core_crm.controller;

import com.petrov.core_crm.dto.BuildingRequest;
import com.petrov.core_crm.dto.BuildingResponse;
import com.petrov.core_crm.entity.Building;
import com.petrov.core_crm.mapper.BuildingMapper;
import com.petrov.core_crm.service.BuildingService;
import com.petrov.core_crm.specification.BuildingSpecifications;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/buildings")
public class BuildingController {

	private final BuildingService buildingService;
	private final BuildingMapper buildingMapper;

	@GetMapping
	public ResponseEntity<Page<BuildingResponse>> getBuildings(
			@RequestParam(required = false) String buildingType,
			@RequestParam(required = false) String cadastrNumber,
			Pageable pageable) {

		Specification<Building> spec = BuildingSpecifications.withFilters(buildingType, cadastrNumber);
		Page<Building> buildings = buildingService.findAll(spec, pageable);
		Page<BuildingResponse> response = buildings.map(buildingMapper::toResponse);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BuildingResponse> getBuilding(@PathVariable Long id) {
		Building building = buildingService.findById(id);
		BuildingResponse response = buildingMapper.toResponse(building);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/by-cadastr/{cadastrNumber}")
	public ResponseEntity<BuildingResponse> getBuildingByCadastr(@PathVariable String cadastrNumber) {
		Building building = buildingService.findByCadastrNumber(cadastrNumber);
		BuildingResponse response = buildingMapper.toResponse(building);
		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<BuildingResponse> createBuilding(@Valid @RequestBody BuildingRequest request) {
		Building building = buildingMapper.toEntity(request);
		Building createdBuilding = buildingService.create(building);
		BuildingResponse response = buildingMapper.toResponse(createdBuilding);
		return ResponseEntity.created(URI.create("/api/buildings/" + createdBuilding.getId()))
				.body(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<BuildingResponse> updateBuilding(
			@PathVariable Long id,
			@Valid @RequestBody BuildingRequest request) {

		Building building = buildingMapper.toEntity(request);
		Building updatedBuilding = buildingService.update(id, building);
		BuildingResponse response = buildingMapper.toResponse(updatedBuilding);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBuilding(@PathVariable Long id) {
		buildingService.delete(id);
		return ResponseEntity.noContent().build();
	}
}