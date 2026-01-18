package com.ey.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
				// ============================
				// CSRF (Disabled for REST)
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

						// ============================
						// PUBLIC READ APIs
						// ============================
						.requestMatchers(HttpMethod.GET, "/api/brands/**", "/api/laptops/**").permitAll()

						.requestMatchers(HttpMethod.POST, "/api/laptops/search").permitAll()

						// ============================
						// PUBLIC SYSTEM ENDPOINTS
						// ============================
						.requestMatchers("/actuator/health", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
						.permitAll()

						// ============================
						// WRITE APIs REQUIRE AUTH
						// ============================
						.requestMatchers("/api/brands/**", "/api/laptops/**").authenticated()

						// ============================
						// EVERYTHING ELSE
						// ============================
						.anyRequest().authenticated());

		return http.build();
	}

	/**
	 * BCrypt password encoder Used by CustomUserDetailsService
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Required for authentication (used later by JWT login endpoint)
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}
