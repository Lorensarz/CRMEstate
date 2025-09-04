package com.petrov.core_crm.specification;

import com.petrov.core_crm.entity.Task;
import com.petrov.core_crm.enums.TaskPriority;
import com.petrov.core_crm.enums.TaskStatus;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TaskSpecifications {

	public static Specification<Task> withFilters(Long assignedTo, Long buildingId,
												  TaskStatus status, TaskPriority priority,
												  LocalDateTime dueDateFrom, LocalDateTime dueDateTo) {
		return ((root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (Objects.nonNull(assignedTo)) {
				predicates.add(cb.equal(root.get("assignedTo").get("id"), assignedTo));
			}
			if (Objects.nonNull(buildingId)) {
				predicates.add(cb.equal(root.get("building").get("id"), buildingId));
			}
			if (Objects.nonNull(status)) {
				predicates.add(cb.equal(root.get("status"), status));
			}
			if (Objects.nonNull(priority)) {
				predicates.add(cb.equal(root.get("priority"), priority));
			}
			if (Objects.nonNull(dueDateFrom)) {
				predicates.add(cb.equal(root.get("dueDatFrom"), dueDateFrom));
			}
			if (Objects.nonNull(dueDateTo)) {
				predicates.add(cb.equal(root.get("dueDatTo"), dueDateTo));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		});

	}
}
