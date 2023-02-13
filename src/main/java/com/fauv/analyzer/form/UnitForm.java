package com.fauv.analyzer.form;

import javax.validation.constraints.NotBlank;

import com.fauv.analyzer.message.UnitMessage;

public class UnitForm {

	@NotBlank(message = UnitMessage.UNIT_FORM_NAME)
	private String name;
	private boolean active = false;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	
}
