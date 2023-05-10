package com.fauv.analyzer.service;

import java.util.List;
import java.util.Set;

import com.fauv.analyzer.entity.MeasurementFm;
import com.fauv.analyzer.entity.MeasurementPmp;
import com.fauv.analyzer.entity.dto.MeasurementFmDTO;
import com.fauv.analyzer.entity.dto.MeasurementPmpDTO;
import com.fauv.analyzer.entity.indicators.FmIndicator;
import com.fauv.analyzer.entity.indicators.PmpIndicator;

public interface CalcService {

	public FmIndicator calcFmIndicatorUsingDTO(Set<MeasurementFmDTO> fmMeasurementDTOList);

	public FmIndicator calcFmIndicator(Set<MeasurementFm> fmMeasurementList);
	
	public FmIndicator calcFmIndicator(List<MeasurementFm> fmMeasurementList);
	
	public PmpIndicator calcPmpIndicatorUsingDTO(Set<MeasurementPmpDTO> measurementPmpDTOList);
	
	public PmpIndicator calcPmpIndicator(Set<MeasurementPmp> measurementPmpList);
	
	public double calcStartandDeviation(List<Double> matValues, double avgMat, int numberOfSamples);
	
	public double calcLsc(double avgMat, double avgMovelRange, int numberOfSamples);
	
	public double calcLic(double avgMat, double avgMovelRange, int numberOfSamples);
	
	public List<Double> calcMovelRange(List<Double> fmListValues);
	
	public double calcCp(double lsc, double lic, double avgMovelRange, double d2);
	
	public double calcCpk(double lsc, double lic, double avgMat, double avgMovelRange, double d2);
	
	public double calcSigmaLevel(double cpk);
	
	public double calcPp(double lsc, double lic, double startandDeviation);
	
	public double calcPpk(double lsc, double lic, double avgMat, double startandDeviation);
	
	public double calcNominalDistributionZ1(double lsc, double avgMat, double standardDeviation, int numberOfSamples);
	
	public double calcNominalDistributionZ2(double lic, double avgMat, double standardDeviation, int numberOfSamples);
}
