package com.petrov.core_crm.repository;

import com.petrov.core_crm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	boolean existsByUserName(String userName);

	boolean existsByEmail(String email);

	User findByUserName(String userName);

}
