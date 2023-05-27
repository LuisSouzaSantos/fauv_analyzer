package com.fauv.analyzer.entity.statistics;

import com.fauv.analyzer.entity.MeasurementAxisCoordinate;

public class DetailedAxisCoordinateGraphicHelper {

	private DetailedGraphic detailedGraphic;
	private MeasurementAxisCoordinate measurementAxisCoordinate;
	
	public DetailedAxisCoordinateGraphicHelper(DetailedGraphic detailedGraphic, MeasurementAxisCoordinate measurementAxisCoordinate) {
		this.detailedGraphic = detailedGraphic;
		this.measurementAxisCoordinate = measurementAxisCoordinate;
	}

	public DetailedGraphic getDetailedGraphic() {
		return detailedGraphic;
	}
	
	public void setDetailedGraphic(DetailedGraphic detailedGraphic) {
		this.detailedGraphic = detailedGraphic;
	}
	
	public MeasurementAxisCoordinate getMeasurementAxisCoordinate() {
		return measurementAxisCoordinate;
	}

	public void setMeasurementAxisCoordinate(MeasurementAxisCoordinate measurementAxisCoordinate) {
		this.measurementAxisCoordinate = measurementAxisCoordinate;
	}
	
}
