package com.fauv.analyzer.exception;

public class ParserException extends BusinessException {

	private static final long serialVersionUID = 1L;
	
	private static final String DEFAULT_MESSAGE = "SERVER_IS_NOT_RUNNING";
	
	public ParserException(String message, boolean useDefaultMessage) {
		super(useDefaultMessage ? DEFAULT_MESSAGE : message);
	}

}
