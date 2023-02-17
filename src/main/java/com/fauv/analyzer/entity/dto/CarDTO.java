package com.fauv.analyzer.entity.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fauv.analyzer.message.CarMessage;

public class CarDTO {

	@NotNull(message = CarMessage.FORM_ID)
	private Long id;
	@NotBlank(message = CarMessage.FORM_NAME)
	private String name;
	private boolean active = false;
	
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
	
}
