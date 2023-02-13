package com.fauv.analyzer.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionHandle {
		
	@ExceptionHandler(value = BusinessException.class)
	protected ResponseEntity<ApiResponseError> handleBusinessException(BusinessException exception, WebRequest request){
		ApiResponseError apiResponseError = new ApiResponseError(exception.getMessage(), exception.getStatus());
		
		return ResponseEntity.status(apiResponseError.getCode().value()).body(apiResponseError);
	}

}
