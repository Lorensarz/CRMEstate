package com.petrov.core_crm.service;

import com.petrov.core_crm.entity.User;
import com.petrov.core_crm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public Page<User> findAll(Specification<User> spec, Pageable pageable) {
		return userRepository.findAll(spec, pageable);
	}

	public User findById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found with id: " + id));
	}

	@Transactional
	public User create(User user) {
		validateUserUniqueness(user);
		return userRepository.save(user);
	}

	@Transactional
	public User update(Long id, User userDetails) {
		User user = findById(id);
		user.setFirstName(userDetails.getFirstName());
		user.setLastName(userDetails.getLastName());
		user.setEmail(userDetails.getEmail());
		return userRepository.save(user);
	}

	@Transactional
	public void delete(Long id) {
		User user = findById(id);
		userRepository.delete(user);
	}

	private void validateUserUniqueness(User user) {
		if (userRepository.existsByUsername(user.getUsername())) {
			throw new RuntimeException("Username already exists: " + user.getUsername());
		}
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new RuntimeException("Email already exists: " + user.getEmail());
		}
	}

}
