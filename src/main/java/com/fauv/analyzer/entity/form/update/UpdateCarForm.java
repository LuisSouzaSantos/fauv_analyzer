package com.fauv.analyzer.entity.form.update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fauv.analyzer.message.CarMessage;
import com.fauv.analyzer.message.EquipmentMessage;

public class UpdateCarForm {
	
	@NotNull(message = CarMessage.FORM_ID)
	private Long id;
	@NotBlank(message = CarMessage.FORM_NAME)
	@Size(max = 255, min = 0, message = EquipmentMessage.FORM_NAME_SIZE)
	private String name;
	private boolean active = false;
	@NotNull(message = CarMessage.FORM_UNIT_ID)
	private Long unitId;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
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
