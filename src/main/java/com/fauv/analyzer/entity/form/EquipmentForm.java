package com.fauv.analyzer.entity.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fauv.analyzer.message.EquipmentMessage;

public class EquipmentForm {

	@NotBlank(message = EquipmentMessage.FORM_NAME)
	@Size(max = 255, min = 0, message = EquipmentMessage.FORM_NAME_SIZE)
	private String name;
	private boolean active = false;
	@NotNull(message = EquipmentMessage.FORM_UNIT_ID)
	private Long unitId;
	
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
	
	public Long getUnitId() {
		return unitId;
	}
	
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	
}
