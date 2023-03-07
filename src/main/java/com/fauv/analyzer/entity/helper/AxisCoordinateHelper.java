package com.fauv.analyzer.entity.helper;

import com.fauv.analyzer.enums.AxisType;

public abstract class AxisCoordinateHelper {

	private String name;
	private Double lowerTolerance;
	private Double superiorTolerance;
	private AxisType axisType;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Double getLowerTolerance() {
		return lowerTolerance;
	}

	public void setLowerTolerance(Double lowerTolerance) {
		this.lowerTolerance = lowerTolerance;
	}

	public Double getSuperiorTolerance() {
		return superiorTolerance;
	}

	public void setSuperiorTolerance(Double superiorTolerance) {
		this.superiorTolerance = superiorTolerance;
	}

	public AxisType getAxisType() {
		return axisType;
	}

	public void setAxisType(AxisType axisType) {
		this.axisType = axisType;
	}

}
