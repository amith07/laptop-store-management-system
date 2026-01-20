package com.ey.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ey.dto.request.ChangePasswordRequest;
import com.ey.dto.request.UpdateProfileRequest;
import com.ey.dto.request.UserRegisterRequest;
import com.ey.dto.response.UserResponse;
import com.ey.enums.ApiErrorCode;
import com.ey.enums.UserStatus;
import com.ey.exception.ApiException;
import com.ey.model.Role;
import com.ey.model.User;
import com.ey.repository.RoleRepository;
import com.ey.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {

		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/* ============================ REGISTER ============================ */

	public UserResponse register(UserRegisterRequest request) {

		if (userRepository.existsByUsername(request.getUsername())) {
			throw new ApiException(ApiErrorCode.USER_ALREADY_EXISTS, HttpStatus.CONFLICT, "Username already exists");
		}

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new ApiException(ApiErrorCode.USER_ALREADY_EXISTS, HttpStatus.CONFLICT, "Email already exists");
		}

		Role customerRole = roleRepository.findByName("CUSTOMER")
				.orElseThrow(() -> new RuntimeException("CUSTOMER role not found"));

		User user = new User(request.getUsername(), request.getEmail(), passwordEncoder.encode(request.getPassword()));

		user.addRole(customerRole);
		userRepository.save(user);

		return new UserResponse(user);
	}

	/* ============================ ME ============================ */

	public UserResponse getMe(String username) {
		return new UserResponse(getUserByUsername(username));
	}

	public UserResponse updateProfile(String username, UpdateProfileRequest request) {
		User user = getUserByUsername(username);
		user.setEmail(request.getEmail());
		return new UserResponse(user);
	}

	public void changePassword(String username, ChangePasswordRequest request) {

		User user = getUserByUsername(username);

		if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
			throw new ApiException(ApiErrorCode.INVALID_PASSWORD, HttpStatus.BAD_REQUEST, "Old password incorrect");
		}

		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
	}

	public UserResponse getUserById(Long id) {
		User user = userRepository.findById(id).orElseThrow(
				() -> new ApiException(ApiErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND, "User not found"));

		return new UserResponse(user);
	}

	public void updateUserRole(Long id, String roleName) {

		User user = userRepository.findById(id).orElseThrow(
				() -> new ApiException(ApiErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND, "User not found"));

		Role role = roleRepository.findByName(roleName).orElseThrow(() -> new RuntimeException("Role not found"));

		user.getRoles().clear();
		user.addRole(role);
	}

	/* ============================ ADMIN ============================ */

	public List<UserResponse> getAllUsers() {
		return userRepository.findAll().stream().map(UserResponse::new).toList();
	}

	public void updateUserStatus(Long userId, UserStatus status) {
		User user = userRepository.findById(userId).orElseThrow(
				() -> new ApiException(ApiErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND, "User not found"));
		user.setStatus(status);
	}

	private User getUserByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow(
				() -> new ApiException(ApiErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND, "User not found"));
	}
}
