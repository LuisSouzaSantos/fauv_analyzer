package com.fauv.analyzer.entity.helper;

public class MeasurementAxisCoordinateHelper extends AxisCoordinateHelper {

	private Double calculated;
	private TolaranceTypeHelper type;

	public Double getCalculated() {
		return calculated;
	}

	public void setCalculated(Double calculated) {
		this.calculated = calculated;
	}

	public TolaranceTypeHelper getType() {
		return type;
	}

	public void setType(TolaranceTypeHelper type) {
		this.type = type;
	}

}
