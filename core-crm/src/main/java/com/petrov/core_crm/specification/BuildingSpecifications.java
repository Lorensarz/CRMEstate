package com.petrov.core_crm.specification;

import com.petrov.core_crm.entity.Building;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BuildingSpecifications {

	public static Specification<Building> withFilters(String buildingType, String cadastrNumber) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (buildingType != null && !buildingType.isEmpty()) {
				predicates.add(cb.equal(root.get("buildingType"), buildingType));
			}
			if (cadastrNumber != null && !cadastrNumber.isEmpty()) {
				predicates.add(cb.like(root.get("cadastrNumber"), "%" + cadastrNumber + "%"));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

	public static Specification<Building> withAddress(String address) {
		return (root, query, cb) -> {
			if (address != null && !address.isEmpty()) {
				return cb.like(cb.lower(root.get("address")), "%" + address.toLowerCase() + "%");
			}
			return null;
		};
	}
}
