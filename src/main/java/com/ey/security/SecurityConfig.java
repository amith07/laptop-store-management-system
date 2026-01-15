package com.ey.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
				// ============================
				// CSRF
				// ============================
				.csrf(csrf -> csrf.disable())

				// ============================
				// Disable UI-based auth (REST only)
				// ============================
				.formLogin(form -> form.disable()).oauth2Login(oauth -> oauth.disable())
				.oauth2Client(oauth -> oauth.disable()).httpBasic(Customizer.withDefaults())

				// ============================
				// Authorization rules
				// ============================
				.authorizeHttpRequests(auth -> auth

						// ---- PUBLIC READ APIs ----
						.requestMatchers(HttpMethod.GET, "/api/brands/**", "/api/laptops/**").permitAll()

						// ---- PUBLIC SYSTEM ENDPOINTS ----
						.requestMatchers("/actuator/health", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
						.permitAll()

						// ---- WRITE APIs REQUIRE AUTH ----
						.requestMatchers("/api/brands/**", "/api/laptops/**").authenticated()

						// ---- EVERYTHING ELSE ----
						.anyRequest().authenticated());

		return http.build();
	}

	/**
	 * TEMPORARY in-memory users (Will be replaced by JWT later)
	 */
	@Bean
	public UserDetailsService userDetailsService() {

		return new InMemoryUserDetailsManager(

				User.withUsername("admin").password("{noop}admin123").roles("ADMIN").build(),

				User.withUsername("manager").password("{noop}manager123").roles("MANAGER").build());
	}
}
