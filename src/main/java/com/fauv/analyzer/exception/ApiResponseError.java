package com.fauv.analyzer.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public class ApiResponseError {
	
	private String message;
	private HttpStatus code;
	private LocalDateTime date;
	
	public ApiResponseError(String message, HttpStatus code) {
		this.message = message;
		this.code = code;
		this.date = LocalDateTime.now();
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public HttpStatus getCode() {
		return code;
	}

	public void setCode(HttpStatus code) {
		this.code = code;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}
}
