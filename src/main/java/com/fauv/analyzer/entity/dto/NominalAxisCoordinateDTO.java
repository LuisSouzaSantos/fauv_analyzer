package com.fauv.analyzer.entity.dto;

import com.fauv.analyzer.enums.AxisType;

public class NominalAxisCoordinateDTO {
	
	private Long id;
	private String name;
	private double lowerTolerance;
	private double higherTolerance;
	private AxisType axis;

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
	
	public double getLowerTolerance() {
		return lowerTolerance;
	}
	
	public void setLowerTolerance(double lowerTolerance) {
		this.lowerTolerance = lowerTolerance;
	}
	
	public double getHigherTolerance() {
		return higherTolerance;
	}
	
	public void setHigherTolerance(double higherTolerance) {
		this.higherTolerance = higherTolerance;
	}

	public AxisType getAxis() {
		return axis;
	}
	
	public void setAxis(AxisType axis) {
		this.axis = axis;
	}

}
