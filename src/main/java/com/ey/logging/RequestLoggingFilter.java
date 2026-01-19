package com.ey.logging;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RequestLoggingFilter extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestId = UUID.randomUUID().toString();
		MDC.put("requestId", requestId);

		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication != null && authentication.isAuthenticated()) {
				MDC.put("username", authentication.getName());
			} else {
				MDC.put("username", "ANONYMOUS");
			}

			log.info("➡️ Incoming request: {} {}", request.getMethod(), request.getRequestURI());

			filterChain.doFilter(request, response);

		} finally {
			log.info("⬅️ Outgoing response: {} {} (status={})", request.getMethod(), request.getRequestURI(),
					response.getStatus());

			MDC.clear();
		}
	}
}
