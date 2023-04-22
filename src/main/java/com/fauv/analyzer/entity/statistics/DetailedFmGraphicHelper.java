package com.fauv.analyzer.entity.statistics;

import com.fauv.analyzer.entity.MeasurementFm;

public class DetailedFmGraphicHelper {

	private DetailedFmGraphic detailedFmGraphic;
	private MeasurementFm measurementFm;
	
	public DetailedFmGraphicHelper(DetailedFmGraphic detailedFmGraphic, MeasurementFm measurementFm) {
		this.detailedFmGraphic = detailedFmGraphic;
		this.measurementFm = measurementFm;
	}

	public DetailedFmGraphic getDetailedFmGraphic() {
		return detailedFmGraphic;
	}
	
	public void setDetailedFmGraphic(DetailedFmGraphic detailedFmGraphic) {
		this.detailedFmGraphic = detailedFmGraphic;
	}
	
	public MeasurementFm getMeasurementFm() {
		return measurementFm;
	}
	
	public void setMeasurementFm(MeasurementFm measurementFm) {
		this.measurementFm = measurementFm;
	}
	
}
