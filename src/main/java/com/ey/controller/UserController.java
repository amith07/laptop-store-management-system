package com.ey.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.request.ChangePasswordRequest;
import com.ey.dto.request.UpdateProfileRequest;
import com.ey.dto.request.UserRegisterRequest;
import com.ey.dto.response.UserResponse;
import com.ey.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	private String currentUser() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	/* ============================ PUBLIC ============================ */

	@PostMapping("/register")
	public ResponseEntity<UserResponse> register(@RequestBody UserRegisterRequest request) {
		return ResponseEntity.ok(userService.register(request));
	}

	/* ============================ AUTH ============================ */

	@GetMapping("/me")
	public ResponseEntity<UserResponse> me() {
		return ResponseEntity.ok(userService.getMe(currentUser()));
	}

	@PutMapping("/me")
	public ResponseEntity<UserResponse> updateProfile(@RequestBody UpdateProfileRequest request) {
		return ResponseEntity.ok(userService.updateProfile(currentUser(), request));
	}

	@PutMapping("/me/password")
	public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request) {
		userService.changePassword(currentUser(), request);
		return ResponseEntity.noContent().build();
	}
}
