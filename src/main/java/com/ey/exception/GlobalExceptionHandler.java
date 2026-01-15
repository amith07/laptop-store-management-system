package com.ey.exception;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ey.dto.response.ErrorResponse;
import com.ey.enums.ApiErrorCode;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

		List<String> details = ex.getBindingResult().getFieldErrors().stream()
				.map(e -> e.getField() + ": " + e.getDefaultMessage()).collect(Collectors.toList());

		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(Instant.now());
		response.setStatus(400);
		response.setError(ApiErrorCode.VALIDATION_FAILED.name());
		response.setMessage("Validation failed");
		response.setDetails(details);

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {

		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(Instant.now());
		response.setStatus(400);
		response.setError(ApiErrorCode.VALIDATION_FAILED.name());
		response.setMessage(ex.getMessage());

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleUnhandled(Exception ex) {
		log.error("Unhandled exception", ex);

		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(Instant.now());
		response.setStatus(500);
		response.setError(ApiErrorCode.INTERNAL_ERROR.name());
		response.setMessage("Unexpected server error");

		return ResponseEntity.internalServerError().body(response);
	}
}
