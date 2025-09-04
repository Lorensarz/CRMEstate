package com.petrov.core_crm.specification;

import com.petrov.core_crm.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecifications {
	public static Specification<User> withFilters(String username, String email) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (username != null && !username.isEmpty()) {
				predicates.add(cb.like(root.get("username"), "%" + username + "%"));
			}
			if (email != null && !email.isEmpty()) {
				predicates.add(cb.like(root.get("email"), "%" + email + "%"));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}
