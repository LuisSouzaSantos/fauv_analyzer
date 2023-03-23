package com.fauv.analyzer.entity.dto;

import java.math.BigDecimal;

import com.fauv.analyzer.enums.AxisType;
import com.fauv.analyzer.enums.ToleranceType;

public class MeasurementAxisCoordinateDTO {

	private String name;
	private BigDecimal value;
	private ToleranceType toleranceType;
	private double lowerTolerance;
	private double higherTolerance;
	private AxisType axis;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public BigDecimal getValue() {
		return value;
	}
	
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	public ToleranceType getToleranceType() {
		return toleranceType;
	}
	
	public void setToleranceType(ToleranceType toleranceType) {
		this.toleranceType = toleranceType;
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
