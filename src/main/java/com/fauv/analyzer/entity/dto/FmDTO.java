package com.fauv.analyzer.entity.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fauv.analyzer.enums.AxisType;

public class FmDTO {
	
	private Long id;
	private String name;
	private double higherTolerance;
	private double lowerTolerance;
	private double defaultValue;
	private AxisType axis;
	@JsonIgnoreProperties(value = {"pmpList", "fmList", "car"})
	private ModelDTO model;
	private List<PmpDTO> pointsUsingToMap = new ArrayList<>();
	
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
	
	public double getHigherTolerance() {
		return higherTolerance;
	}
	
	public void setHigherTolerance(double higherTolerance) {
		this.higherTolerance = higherTolerance;
	}
	
	public double getLowerTolerance() {
		return lowerTolerance;
	}
	
	public void setLowerTolerance(double lowerTolerance) {
		this.lowerTolerance = lowerTolerance;
	}
	
	public double getDefaultValue() {
		return defaultValue;
	}
	
	public void setDefaultValue(double defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public AxisType getAxis() {
		return axis;
	}
	
	public void setAxis(AxisType axis) {
		this.axis = axis;
	}
	
	public ModelDTO getModel() {
		return model;
	}

	public void setModel(ModelDTO model) {
		this.model = model;
	}

	public List<PmpDTO> getPointsUsingToMap() {
		return pointsUsingToMap;
	}
	
	public void setPointsUsingToMap(List<PmpDTO> pointsUsingToMap) {
		this.pointsUsingToMap = pointsUsingToMap;
	}
	
}
