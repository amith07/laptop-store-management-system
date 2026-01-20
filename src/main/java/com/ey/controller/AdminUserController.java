package com.ey.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ey.dto.response.UserResponse;
import com.ey.enums.UserStatus;
import com.ey.service.UserService;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

	private final UserService userService;

	public AdminUserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}

	@PutMapping("/{id}/status")
	public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam UserStatus status) {

		userService.updateUserStatus(id, status);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/role")
	public ResponseEntity<Void> updateRole(@PathVariable Long id, @RequestParam String role) {

		userService.updateUserRole(id, role);
		return ResponseEntity.noContent().build();
	}
}
