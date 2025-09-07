package com.petrov.core_crm.repository;

import com.petrov.core_crm.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Long>, JpaSpecificationExecutor<Building> {

	boolean existsByCadastrNumber(String cadastrNumber);

	Optional<Building> findByCadastrNumber(String cadastrNumber);

	@Query("SELECT b FROM Building b WHERE b.updatedAt < :thresholdDate")
	List<Building> findBuildingsWithNoRecentActivity(LocalDateTime thresholdDate);
}
