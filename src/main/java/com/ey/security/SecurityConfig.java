package com.ey.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ey.logging.RequestLoggingFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenProvider jwtTokenProvider)
			throws Exception {

		http
				// ============================
				// Stateless REST API
				// ============================
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// ============================
				// Disable stateful auth
				// ============================
				.httpBasic(httpBasic -> httpBasic.disable()).formLogin(form -> form.disable())
				.oauth2Login(oauth -> oauth.disable()).oauth2Client(oauth -> oauth.disable())

				// ============================
				// Logging filter (FIRST)
				// ============================
				.addFilterBefore(new RequestLoggingFilter(), UsernamePasswordAuthenticationFilter.class)

				// ============================
				// JWT filter
				// ============================
				.addFilterBefore(jwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)

				// ============================
				// Authorization rules
				// ============================
				.authorizeHttpRequests(auth -> auth

						// PUBLIC
						.requestMatchers("/auth/login", "/users/register").permitAll()

						// CUSTOMER
						.requestMatchers("/api/cart/**", "/api/orders/**").hasAuthority("ROLE_CUSTOMER")

						// MANAGER
						.requestMatchers("/api/manager/**").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN")

						// ADMIN
						.requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")

						// Public read
						.requestMatchers(HttpMethod.GET, "/api/brands/**", "/api/laptops/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/laptops/search").permitAll()

						// Docs
						.requestMatchers("/actuator/health", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
						.permitAll()

						.anyRequest().authenticated());

		return http.build();
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
		return new JwtAuthenticationFilter(jwtTokenProvider);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Used ONLY by /auth/login
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}
