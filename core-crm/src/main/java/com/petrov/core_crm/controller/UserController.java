package com.petrov.core_crm.controller;

import com.petrov.core_crm.dto.UserRequest;
import com.petrov.core_crm.dto.UserResponse;
import com.petrov.core_crm.entity.User;
import com.petrov.core_crm.mapper.UserMapper;
import com.petrov.core_crm.service.UserService;
import com.petrov.core_crm.specification.UserSpecifications;
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
@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;
	private final UserMapper userMapper;

	@GetMapping
	public ResponseEntity<Page<UserResponse>> getUsers(
			@RequestParam(required = false) String username,
			@RequestParam(required = false) String email,
			Pageable pageable) {

		Specification<User> spec = UserSpecifications.withFilters(username, email);
		Page<User> users = userService.findAll(spec, pageable);
		Page<UserResponse> response = users.map(userMapper::toResponse);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
		User user = userService.findById(id);
		UserResponse response = userMapper.toResponse(user);
		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
		User user = userMapper.toEntity(request);
		User createdUser = userService.create(user);
		UserResponse response = userMapper.toResponse(createdUser);
		return ResponseEntity.created(URI.create("/api/users/" + createdUser.getId()))
				.body(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserResponse> updateUser(
			@PathVariable Long id,
			@Valid @RequestBody UserRequest request) {

		User user = userMapper.toEntity(request);
		User updatedUser = userService.update(id, user);
		UserResponse response = userMapper.toResponse(updatedUser);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
