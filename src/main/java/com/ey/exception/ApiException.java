package com.ey.exception;

import org.springframework.http.HttpStatus;

import com.ey.enums.ApiErrorCode;

public class ApiException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final ApiErrorCode errorCode;
    private final HttpStatus status;

    public ApiException(ApiErrorCode errorCode, HttpStatus status, String message) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public ApiErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
