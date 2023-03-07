package com.fauv.analyzer.entity.helper;

import com.fauv.analyzer.enums.AxisType;

public class CoordinateValueHelper {

	private double value;
	private AxisType axis;
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public AxisType getAxisType() {
		return axis;
	}
	
	public void setAxisType(AxisType axis) {
		this.axis = axis;
	}
	
}
