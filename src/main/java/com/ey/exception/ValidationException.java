package com.ey.exception;

import org.springframework.http.HttpStatus;

import com.ey.enums.ApiErrorCode;

public class ValidationException extends ApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ValidationException(String message) {
		super(ApiErrorCode.VALIDATION_FAILED, HttpStatus.BAD_REQUEST, message);
	}
}
