package com.fauv.analyzer.service;

import java.util.List;

import com.fauv.analyzer.entity.MeasurementFm;
import com.fauv.analyzer.entity.statistics.CepIndividualValuesGraphic;
import com.fauv.analyzer.entity.statistics.CepMovelAmplitudeGraphic;
import com.fauv.analyzer.entity.statistics.IndividualValuesGraphic;
import com.fauv.analyzer.entity.statistics.MovelAmplitudeGraphic;

public interface GraphicService {

	public CepIndividualValuesGraphic buildCepIndividualValuesGraphic(double higherTolerance, double lowerTolerance, double midline, double avgMat, double valueToCalcateZone, List<MeasurementFm> measurementFmList);
	
	public CepMovelAmplitudeGraphic buildCepMovelAmplitudeGraphic(double higherTolerance, double lowerTolerance, double midline, List<MeasurementFm> measurementFmList, List<Double> movelRange);
	
	public IndividualValuesGraphic buildIndividualGraphic(double higherTolerance, double lowerTolerance, double midline, List<MeasurementFm> measurementFmList, List<Double> mat);
	
	public MovelAmplitudeGraphic buildMovelAmplideGraphic(double higherTolerance, double lowerTolerance, double midline, List<MeasurementFm> measurementFmList, List<Double> movelRange);
	
}
