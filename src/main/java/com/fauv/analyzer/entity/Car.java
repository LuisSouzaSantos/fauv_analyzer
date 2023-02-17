package com.fauv.analyzer.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fauv.analyzer.message.EquipmentMessage;

@Entity
@Table(name = "car", schema = "analyzer")
public class Car {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull(message = EquipmentMessage.FORM_ID)
	private Long id;
	@NotBlank(message = EquipmentMessage.FORM_NAME)
	@Size(max = 255, min = 0, message = EquipmentMessage.FORM_NAME_SIZE)
	private String name;
	private boolean active = false;
	@NotNull(message = EquipmentMessage.FORM_UNIT_ID)
    @ManyToOne
    @JoinColumn(name = "unit_id")
	private Unit unit;
	
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
	
	public Unit getUnit() {
		return unit;
	}
	
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
}
