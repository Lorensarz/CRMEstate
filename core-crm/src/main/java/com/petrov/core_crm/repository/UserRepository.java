package com.petrov.core_crm.repository;

import com.petrov.core_crm.entity.User;
import com.petrov.core_crm.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	User findByUsername(String username);
	List<User> findUsersByRole(UserRole role);

}
