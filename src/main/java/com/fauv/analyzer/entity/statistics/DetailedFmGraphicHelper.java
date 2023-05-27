package com.fauv.analyzer.entity.statistics;

import com.fauv.analyzer.entity.MeasurementFm;

public class DetailedFmGraphicHelper {

	private DetailedGraphic detailedFmGraphic;
	private MeasurementFm measurementFm;
	
	public DetailedFmGraphicHelper(DetailedGraphic detailedFmGraphic, MeasurementFm measurementFm) {
		this.detailedFmGraphic = detailedFmGraphic;
		this.measurementFm = measurementFm;
	}

	public DetailedGraphic getDetailedFmGraphic() {
		return detailedFmGraphic;
	}
	
	public void setDetailedFmGraphic(DetailedGraphic detailedFmGraphic) {
		this.detailedFmGraphic = detailedFmGraphic;
	}
	
	public MeasurementFm getMeasurementFm() {
		return measurementFm;
	}
	
	public void setMeasurementFm(MeasurementFm measurementFm) {
		this.measurementFm = measurementFm;
	}
	
}
