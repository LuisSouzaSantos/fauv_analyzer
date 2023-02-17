package com.fauv.analyzer.entity.form;

import javax.validation.constraints.NotBlank;

import com.fauv.analyzer.message.CarMessage;

public class CarForm {

	@NotBlank(message = CarMessage.FORM_NAME)
	private String name;
	private Long unitId;
	private boolean active = false;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
}
