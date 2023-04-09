package com.fauv.analyzer.entity.helper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fauv.analyzer.enums.AxisType;
import com.fauv.analyzer.message.DMOMessage;

public abstract class AxisCoordinateHelper {

	@NotBlank(message = DMOMessage.AXIS_COORDINATE_NAME)
	private String name;
	@NotNull(message = DMOMessage.AXIS_COORDINATE_LOWER_TOLERANCE)
	private Double lowerTolerance;
	@NotNull(message = DMOMessage.AXIS_COORDINATE_SUPERIOR_TOLERANCE)
	private Double superiorTolerance;
	@NotNull(message = DMOMessage.AXIS_COORDINATE_AXIS)
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
