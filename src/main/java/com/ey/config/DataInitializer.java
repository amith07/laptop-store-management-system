package com.ey.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ey.model.Role;
import com.ey.model.User;
import com.ey.repository.RoleRepository;
import com.ey.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Configuration
public class DataInitializer {

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public DataInitializer(RoleRepository roleRepository, UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@PostConstruct
	public void init() {

		Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> roleRepository.save(new Role("ADMIN")));

		Role managerRole = roleRepository.findByName("MANAGER")
				.orElseGet(() -> roleRepository.save(new Role("MANAGER")));

		Role customerRole = roleRepository.findByName("CUSTOMER")
				.orElseGet(() -> roleRepository.save(new Role("CUSTOMER")));

		if (!userRepository.existsByUsername("admin")) {
			User admin = new User("admin", "admin@store.com", passwordEncoder.encode("admin123"));
			admin.addRole(adminRole);
			userRepository.save(admin);
		}

		if (!userRepository.existsByUsername("manager")) {
			User manager = new User("manager", "manager@store.com", passwordEncoder.encode("manager123"));
			manager.addRole(managerRole);
			userRepository.save(manager);
		}

		if (!userRepository.existsByUsername("customer")) {
			User customer = new User("customer", "customer@store.com", passwordEncoder.encode("customer123"));
			customer.addRole(customerRole);
			userRepository.save(customer);
		}
	}
}
