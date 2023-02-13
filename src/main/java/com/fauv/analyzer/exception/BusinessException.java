package com.fauv.analyzer.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
	
	public BusinessException(String message) {
		super(message);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	
}
