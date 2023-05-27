package com.fauv.analyzer.service;

import java.util.Collection;
import java.util.List;

import com.fauv.analyzer.entity.MeasurementAxisCoordinate;
import com.fauv.analyzer.entity.MeasurementFm;
import com.fauv.analyzer.entity.MeasurementPmp;
import com.fauv.analyzer.entity.dto.MeasurementFmDTO;
import com.fauv.analyzer.entity.dto.MeasurementPmpDTO;
import com.fauv.analyzer.entity.indicators.AxisIndicator;
import com.fauv.analyzer.entity.indicators.FmIndicator;
import com.fauv.analyzer.entity.indicators.PmpIndicator;
import com.fauv.analyzer.enums.ToleranceTypeStatus;

public interface CalcService {

	public FmIndicator calcFmIndicatorUsingDTO(Collection<MeasurementFmDTO> fmMeasurementDTOList);

	public FmIndicator calcFmIndicator(Collection<MeasurementFm> fmMeasurementList);
	
	public ToleranceTypeStatus getToleranceTypeStatus(double higherTolerance, double lowerTolerance, double value);
	
	public PmpIndicator calcPmpIndicatorUsingDTO(Collection<MeasurementPmpDTO> measurementPmpDTOList);
	
	public PmpIndicator calcPmpIndicator(Collection<MeasurementPmp> measurementPmpList);
	
	public AxisIndicator calcAxisIndicatorOnlyOneAxis(Collection<MeasurementAxisCoordinate> measurementAxisCoordinateList);
	
	public double calcStartandDeviation(List<Double> matValues, double avgMat, int numberOfSamples);
	
	public double calcLsc(double avgMat, double avgMovelRange, int numberOfSamples);
	
	public double calcLic(double avgMat, double avgMovelRange, int numberOfSamples);
	
	public List<Double> calcMovelRange(List<Double> fmListValues);
	
	public double calcCp(double lsc, double lic, double avgMovelRange, double d2);
	
	public double calcCpk(double lsc, double lic, double avgMat, double avgMovelRange, double d2);
	
	public double calcSigmaLevel(double cpk);
	
	public double calcPp(double lsc, double lic, double startandDeviation);
	
	public double calcPpk(double lsc, double lic, double avgMat, double startandDeviation);
	
	public boolean calcIsProcessIsAble(double cp, double cpk, double pp, double ppk);
}
