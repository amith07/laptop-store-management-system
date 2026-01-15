package com.ey.exception;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ey.dto.response.ErrorResponse;
import com.ey.enums.ApiErrorCode;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	// ============================
	// Custom API Exceptions
	// ============================
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ErrorResponse> handleApiException(ApiException ex) {
		log.error("API Exception: {}", ex.getMessage());

		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(Instant.now());
		response.setStatus(ex.getStatus().value());
		response.setError(ex.getErrorCode().name());
		response.setMessage(ex.getMessage());

		return ResponseEntity.status(ex.getStatus()).body(response);
	}

	// ============================
	// Validation Errors
	// ============================
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

		List<String> details = ex.getBindingResult().getFieldErrors().stream().map(e -> e.getField() + ": " + e.getDefaultMessage()).collect(Collectors.toList());

		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(Instant.now());
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setError(ApiErrorCode.VALIDATION_FAILED.name());
		response.setMessage("Validation failed");
		response.setDetails(details);

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {

		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(Instant.now());
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setError(ApiErrorCode.VALIDATION_FAILED.name());
		response.setMessage(ex.getMessage());

		return ResponseEntity.badRequest().body(response);
	}

	// ============================
	// SECURITY EXCEPTIONS
	// ============================
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
		log.warn("Access denied: {}", ex.getMessage());

		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(Instant.now());
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setError(ApiErrorCode.ACCESS_DENIED.name());
		response.setMessage("Access is denied");

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException ex) {
		log.warn("Authentication failed: {}", ex.getMessage());

		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(Instant.now());
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setError(ApiErrorCode.AUTH_REQUIRED.name());
		response.setMessage("Authentication required");

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	// ============================
	// Fallback
	// ============================
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleUnhandled(Exception ex) {
		log.error("Unhandled exception", ex);

		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(Instant.now());
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.setError(ApiErrorCode.INTERNAL_ERROR.name());
		response.setMessage("Unexpected server error");

		return ResponseEntity.internalServerError().body(response);
	}

}
