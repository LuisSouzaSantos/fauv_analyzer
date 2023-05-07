package com.fauv.analyzer.entity.dto;

public class TokenValidationResponse {

	private boolean valid = false;
	private boolean timeToExpire;
	
	public boolean isValid() {
		return valid;
	}
	
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	public boolean isTimeToExpire() {
		return timeToExpire;
	}
	
	public void setTimeToExpire(boolean timeToExpire) {
		this.timeToExpire = timeToExpire;
	}
	
}
