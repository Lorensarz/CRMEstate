package com.petrov.core_crm.service;

import com.petrov.core_crm.entity.Building;
import com.petrov.core_crm.repository.BuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BuildingService {

	private final BuildingRepository buildingRepository;

	public Page<Building> findAll(Specification<Building> spec, Pageable pageable) {
		return buildingRepository.findAll(spec, pageable);
	}

	public Building findById(Long id) {
		return buildingRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Building not found with id: " + id));
	}

	public Building findByCadastrNumber(String cadastrNumber) {
		return buildingRepository.findByCadastrNumber(cadastrNumber)
				.orElseThrow(() -> new RuntimeException("Building not found with cadastr number: " + cadastrNumber));
	}

	@Transactional
	public Building create(Building building) {
		validateBuildingUniqueness(building);
		return buildingRepository.save(building);
	}

	@Transactional
	public Building update(Long id, Building buildingDetails) {
		Building building = findById(id);
		building.setAddress(buildingDetails.getAddress());
		building.setBuildingType(buildingDetails.getBuildingType());
		building.setCadastrNumber(buildingDetails.getCadastrNumber());
		return buildingRepository.save(building);
	}

	@Transactional
	public void delete(Long id) {
		Building building = findById(id);
		buildingRepository.delete(building);
	}

	private void validateBuildingUniqueness(Building building) {
		if (buildingRepository.existsByCadastrNumber(building.getCadastrNumber())) {
			throw new RuntimeException("Building with cadastr number already exists: " + building.getCadastrNumber());
		}
	}
}
