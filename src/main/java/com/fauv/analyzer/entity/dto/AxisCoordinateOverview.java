package com.fauv.analyzer.entity.dto;

import com.fauv.analyzer.enums.AxisType;
import com.fauv.analyzer.enums.ToleranceType;
import com.fauv.analyzer.enums.ToleranceTypeStatus;

public class AxisCoordinateOverview {

	private String name;
	private double value;
	private AxisType axis;
	private ToleranceType toleranceType;
	private ToleranceTypeStatus toleranceStatus;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public AxisType getAxis() {
		return axis;
	}
	
	public void setAxis(AxisType axis) {
		this.axis = axis;
	}
	
	public ToleranceType getToleranceType() {
		return toleranceType;
	}
	
	public void setToleranceType(ToleranceType toleranceType) {
		this.toleranceType = toleranceType;
	}
	
	public ToleranceTypeStatus getToleranceStatus() {
		return toleranceStatus;
	}
	
	public void setToleranceStatus(ToleranceTypeStatus toleranceStatus) {
		this.toleranceStatus = toleranceStatus;
	}
	
}
