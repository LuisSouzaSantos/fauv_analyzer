package com.fauv.analyzer.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.springframework.stereotype.Service;

import com.fauv.analyzer.entity.MeasurementAxisCoordinate;
import com.fauv.analyzer.entity.MeasurementFm;
import com.fauv.analyzer.entity.MeasurementPmp;
import com.fauv.analyzer.entity.NominalAxisCoordinate;
import com.fauv.analyzer.entity.NominalFm;
import com.fauv.analyzer.entity.dto.MeasurementAxisCoordinateDTO;
import com.fauv.analyzer.entity.dto.MeasurementFmDTO;
import com.fauv.analyzer.entity.dto.MeasurementPmpDTO;
import com.fauv.analyzer.entity.indicators.AxisIndicator;
import com.fauv.analyzer.entity.indicators.FmIndicator;
import com.fauv.analyzer.entity.indicators.PmpIndicator;
import com.fauv.analyzer.enums.AxisType;
import com.fauv.analyzer.enums.D;
import com.fauv.analyzer.enums.ToleranceTypeStatus;
import com.fauv.analyzer.service.CalcService;

@Service
public class CalcServiceImpl implements CalcService {

	private static final double BK_PERCENT = 0.75;
	private static final double MAX_CP_VALUE = 1.0;
	private static final double MAX_CPK_VALUE = 1.0;
	private static final double MAX_SIGMA_LEVEL_VALUE = 6.0;
	private static final double MAX_PP_VALUE = 1.0;
	private static final double MAX_PPK_VALUE = 1.0;
	
	@Override
	public FmIndicator calcFmIndicatorUsingDTO(Collection<MeasurementFmDTO> fmMeasurementDTOList) {
		FmIndicator indicator = new FmIndicator();
		
		for (MeasurementFmDTO fmMeasurement : fmMeasurementDTOList) {
			double matValue = fmMeasurement.getValue()-fmMeasurement.getDefaultValue();
			
			ToleranceTypeStatus toleranceTypeStatus = getToleranceTypeStatus(fmMeasurement.getHigherTolerance(), fmMeasurement.getLowerTolerance(), matValue);
			
			if (toleranceTypeStatus.equals(ToleranceTypeStatus.AK)) { indicator.setAk(indicator.getAk()+1); }
			else if (toleranceTypeStatus.equals(ToleranceTypeStatus.BK)) { indicator.setBk(indicator.getBk()+1); }
			else { indicator.setIo(indicator.getIo()+1); }
		}
		
		return indicator;
	}

	@Override
	public FmIndicator calcFmIndicator(Collection<MeasurementFm> fmMeasurementList) {
		FmIndicator indicator = new FmIndicator();
		
		for (MeasurementFm fmMeasurement : fmMeasurementList) {
			NominalFm nominalFm = fmMeasurement.getNominalFm();

			double matValue = fmMeasurement.getValue().doubleValue()-nominalFm.getDefaultValue().doubleValue();
						
			ToleranceTypeStatus toleranceTypeStatus = getToleranceTypeStatus(nominalFm.getHigherTolerance().doubleValue(), nominalFm.getLowerTolerance().doubleValue(), matValue);
			
			if (toleranceTypeStatus.equals(ToleranceTypeStatus.AK)) { indicator.setAk(indicator.getAk()+1); }
			else if (toleranceTypeStatus.equals(ToleranceTypeStatus.BK)) { indicator.setBk(indicator.getBk()+1); }
			else { indicator.setIo(indicator.getIo()+1); }
		}
		
		return indicator;
	}
	
	@Override
	public ToleranceTypeStatus getToleranceTypeStatus(double higherTolerance, double lowerTolerance, double value) {
		boolean isAk = isAk(higherTolerance, lowerTolerance, value);
		
		if (isAk) { return ToleranceTypeStatus.AK; }
		
		boolean isBk = isBk(higherTolerance, lowerTolerance, value);
		
		if (isBk) { 
			return ToleranceTypeStatus.BK; 
		}
		
		return ToleranceTypeStatus.IO;
	}
	
	private boolean isAk(double higherTolerance, double lowerTolerance, double value) {
		boolean valueIsPositive = value > 0.0;
		
		boolean isBothPositive = higherTolerance > 0.0 && lowerTolerance > 0.0;
		boolean isBothNegative = higherTolerance < 0.0 && lowerTolerance < 0.0;
		
		if (isBothPositive) {
			if (valueIsPositive) {
				return value > higherTolerance || value < lowerTolerance;
			}else {
				return true;
			}
		}else if (isBothNegative) {
			if (valueIsPositive) {
				return true;
			}else {
				return value < lowerTolerance || value > higherTolerance;
			}
		}else {
			if (valueIsPositive) {
				return value > higherTolerance;
			}else {
				return value < lowerTolerance;
			}
		}
				
	}
	
	private boolean isBk(double higherTolerance, double lowerTolerance, double value) {
		boolean valueIsPositive = value > 0.0;
		
		double higherToleranceBkTolerance = BK_PERCENT*higherTolerance;
		double lowerToleranceBkTolerance = BK_PERCENT*lowerTolerance;
		
		boolean isBothPositive = higherTolerance > 0.0 && lowerTolerance > 0.0;
		boolean isBothNegative = higherTolerance < 0.0 && lowerTolerance < 0.0;
		
		if (isBothPositive) {
			if (valueIsPositive) {
				return (value <= higherTolerance && value >= higherToleranceBkTolerance) || 
						(value <= lowerTolerance && value >= lowerToleranceBkTolerance);
			}else {
				return true;
			}
		}else if (isBothNegative) {
			if (valueIsPositive) {
				return true;
			}else {
				return (value >= lowerTolerance && value <= lowerToleranceBkTolerance) || 
						(value >= higherTolerance && value <= higherToleranceBkTolerance);
			}
		}else {
			if (valueIsPositive) {
				return (value <= higherTolerance && value >= higherToleranceBkTolerance);
			}else {
				return (value >= lowerTolerance && value <= lowerToleranceBkTolerance);
			}
		}
	}
	

	@Override
	public PmpIndicator calcPmpIndicatorUsingDTO(Collection<MeasurementPmpDTO> measurementPmpDTOList) {
		PmpIndicator indicator = new PmpIndicator();
		
		List<MeasurementAxisCoordinateDTO> measurementAxisCoordinateList =	measurementPmpDTOList
				.stream()
				.flatMap(measurementPmp -> measurementPmp.getMeasurementAxisCoordinateList().stream())
				.collect(Collectors.toList());
		
		for (MeasurementAxisCoordinateDTO measurementAxisCoordinate : measurementAxisCoordinateList) {
			ToleranceTypeStatus toleranceTypeStatus = getToleranceTypeStatus(
					measurementAxisCoordinate.getHigherTolerance(), measurementAxisCoordinate.getLowerTolerance(),
					measurementAxisCoordinate.getValue().doubleValue());
			
			if (measurementAxisCoordinate.getAxis().equals(AxisType.X)) {
				if (toleranceTypeStatus.equals(ToleranceTypeStatus.AK)) { indicator.setAkT(indicator.getAkT()+1);}
				else if(toleranceTypeStatus.equals(ToleranceTypeStatus.BK)) { indicator.setBkT(indicator.getBkT()+1); }
				else { indicator.setIoT(indicator.getIoT()+1); }
			}else if(measurementAxisCoordinate.getAxis().equals(AxisType.Y)) {
				if (toleranceTypeStatus.equals(ToleranceTypeStatus.AK)) { indicator.setAkD(indicator.getAkD()+1);}
				else if(toleranceTypeStatus.equals(ToleranceTypeStatus.BK)) { indicator.setBkD(indicator.getBkD()+1); }
				else { indicator.setIoD(indicator.getIoD()+1); }
			}else if (measurementAxisCoordinate.getAxis().equals(AxisType.Z)) {
				if (toleranceTypeStatus.equals(ToleranceTypeStatus.AK)) { indicator.setAkZ(indicator.getAkZ()+1);}
				else if(toleranceTypeStatus.equals(ToleranceTypeStatus.BK)) { indicator.setBkZ(indicator.getBkZ()+1); }
				else { indicator.setIoZ(indicator.getIoZ()+1); }
			}else if (measurementAxisCoordinate.getAxis().equals(AxisType.D)) {
				if (toleranceTypeStatus.equals(ToleranceTypeStatus.AK)) { indicator.setAkY(indicator.getAkY()+1);}
				else if(toleranceTypeStatus.equals(ToleranceTypeStatus.BK)) { indicator.setBkY(indicator.getBkY()+1); }
				else { indicator.setIoY(indicator.getIoY()+1); }
			}else {
				if (toleranceTypeStatus.equals(ToleranceTypeStatus.AK)) { indicator.setAkX(indicator.getAkX()+1);}
				else if(toleranceTypeStatus.equals(ToleranceTypeStatus.BK)) { indicator.setBkX(indicator.getBkX()+1); }
				else { indicator.setIoX(indicator.getIoX()+1); }
			}
				
		}
		
		return indicator;
	}

	@Override
	public PmpIndicator calcPmpIndicator(Collection<MeasurementPmp> measurementPmpList) {
		PmpIndicator indicator = new PmpIndicator();
		
		List<MeasurementAxisCoordinate> measurementAxisCoordinateList =	measurementPmpList
				.stream()
				.flatMap(measurementPmp -> measurementPmp.getMeasurementAxisCoordinateList().stream())
				.collect(Collectors.toList());
		
		for (MeasurementAxisCoordinate measurementAxisCoordinate : measurementAxisCoordinateList) {
			NominalAxisCoordinate nominalAxisCoordinate = measurementAxisCoordinate.getNominalAxisCoordinate();
			
			ToleranceTypeStatus toleranceTypeStatus = getToleranceTypeStatus(
					nominalAxisCoordinate.getHigherTolerance().doubleValue(), nominalAxisCoordinate.getLowerTolerance().doubleValue(),
					measurementAxisCoordinate.getValue().doubleValue());
							
			if (nominalAxisCoordinate.getAxis().equals(AxisType.X)) {
				if (toleranceTypeStatus.equals(ToleranceTypeStatus.AK)) { indicator.setAkT(indicator.getAkT()+1);}
				else if(toleranceTypeStatus.equals(ToleranceTypeStatus.BK)) { indicator.setBkT(indicator.getBkT()+1); }
				else { indicator.setIoT(indicator.getIoT()+1); }
			}else if(nominalAxisCoordinate.getAxis().equals(AxisType.Y)) {
				if (toleranceTypeStatus.equals(ToleranceTypeStatus.AK)) { indicator.setAkD(indicator.getAkD()+1);}
				else if(toleranceTypeStatus.equals(ToleranceTypeStatus.BK)) { indicator.setBkD(indicator.getBkD()+1); }
				else { indicator.setIoD(indicator.getIoD()+1); }
			}else if (nominalAxisCoordinate.getAxis().equals(AxisType.Z)) {
				if (toleranceTypeStatus.equals(ToleranceTypeStatus.AK)) { indicator.setAkZ(indicator.getAkZ()+1);}
				else if(toleranceTypeStatus.equals(ToleranceTypeStatus.BK)) { indicator.setBkZ(indicator.getBkZ()+1); }
				else { indicator.setIoZ(indicator.getIoZ()+1); }
			}else if (nominalAxisCoordinate.getAxis().equals(AxisType.D)) {
				if (toleranceTypeStatus.equals(ToleranceTypeStatus.AK)) { indicator.setAkY(indicator.getAkY()+1);}
				else if(toleranceTypeStatus.equals(ToleranceTypeStatus.BK)) { indicator.setBkY(indicator.getBkY()+1); }
				else { indicator.setIoY(indicator.getIoY()+1); }
			}else {
				if (toleranceTypeStatus.equals(ToleranceTypeStatus.AK)) { indicator.setAkX(indicator.getAkX()+1);}
				else if(toleranceTypeStatus.equals(ToleranceTypeStatus.BK)) { indicator.setBkX(indicator.getBkX()+1); }
				else { indicator.setIoX(indicator.getIoX()+1); }
			}
		}
		
		return indicator;
	}
	
	@Override
	public AxisIndicator calcAxisIndicatorOnlyOneAxis(Collection<MeasurementAxisCoordinate> measurementAxisCoordinateList) {
		AxisIndicator indicator = new AxisIndicator();
		
		for (MeasurementAxisCoordinate measurementAxisCoordinate : measurementAxisCoordinateList) {
			NominalAxisCoordinate nominalAxisCoordinate = measurementAxisCoordinate.getNominalAxisCoordinate();
	
			ToleranceTypeStatus toleranceTypeStatus = getToleranceTypeStatus(
					nominalAxisCoordinate.getHigherTolerance().doubleValue(), nominalAxisCoordinate.getLowerTolerance().doubleValue(),
					measurementAxisCoordinate.getValue().doubleValue());
			
			if (toleranceTypeStatus.equals(ToleranceTypeStatus.AK)) { indicator.setAk(indicator.getAk()+1); }
			if (toleranceTypeStatus.equals(ToleranceTypeStatus.BK)) { indicator.setBk(indicator.getBk()+1); }
			if (toleranceTypeStatus.equals(ToleranceTypeStatus.IO)) { indicator.setIo(indicator.getIo()+1); }
			
		}
		
		return indicator;
	}
	
	public double calcStartandDeviation(List<Double> matValues, double avgMat, int numberOfSamples) {
		double totalSum = matValues.stream().reduce(0.0, (currentSum, currentValue) -> (currentSum) + (Math.pow(currentValue-avgMat, 2)));
		
		return Math.sqrt(totalSum/(numberOfSamples-1));
	}
	
	public double calcLsc(double avgMat, double avgMovelRange, int numberOfSamples) {
		double d2Constant = D.D2.getConstant(numberOfSamples);
		
		return avgMat+3*(avgMovelRange/d2Constant);	
	}
	
	public double calcLic(double avgMat, double avgMovelRange, int numberOfSamples) {		
		double d2Constant = D.D2.getConstant(numberOfSamples);
		
		return avgMat-3*(avgMovelRange/d2Constant);	
	}
	
	public List<Double> calcMovelRange(List<Double> fmListValues) {
		List<Double> movelRange = new ArrayList<>();
				
		for (int i = 0; i < fmListValues.size(); i++) {
			double value = fmListValues.get(i);
			
			double previousValue = 0.0;
			
			if (i == 0) {
				movelRange.add(previousValue);
				continue; 
			}
			
			previousValue = fmListValues.get(i-1);
			
			double maxValue = Double.max(value, previousValue);
			double minValue = Double.min(value, previousValue);
			double sum = maxValue-minValue;
			
			movelRange.add(sum >= 0 ? sum : sum*-1);
		}
		
		return movelRange;
	}
	
	public double calcCp(double lsc, double lic, double avgMovelRange, double d2) {
		double cp = (lsc-lic)/(6*(avgMovelRange/d2));
		
		return Double.isInfinite(cp) ? MAX_CP_VALUE : cp;
	}
	
	public double calcCpk(double lsc, double lic, double avgMat, double avgMovelRange, double d2) {
		double cpi = (avgMat-lic)/(3*(avgMovelRange*d2));
		double cps = (lsc-avgMat)/(3*(avgMovelRange*d2));
		
		double cpk = Double.min(cpi, cps);
		
		return Double.isInfinite(cpk) ? MAX_CPK_VALUE : cpk;
	}
	
	public double calcSigmaLevel(double cpk) {
		double sigmaLevel = 3*cpk;
		
		return Double.isInfinite(sigmaLevel) ? MAX_SIGMA_LEVEL_VALUE : sigmaLevel;
	}
	
	public double calcPp(double lsc, double lic, double startandDeviation) {
		double pp = (lsc-lic)/(6*startandDeviation);
		
		return Double.isInfinite(pp) ? MAX_PP_VALUE : pp; 
	}
	
	public double calcPpk(double lsc, double lic, double avgMat, double startandDeviation) {		
		double ppi = (avgMat-lic)/(3*startandDeviation);
		double pps = (lsc-avgMat)/(3*startandDeviation);
		
		double ppK = Double.min(ppi, pps);
		
		return Double.isInfinite(ppK) ? MAX_PPK_VALUE : ppK;  
	}
	
	public static double calculateNormalDistribution(double x, double mean, double standardDeviation) {
		NormalDistribution normalDistribution = new NormalDistribution(mean, standardDeviation);
		
		return normalDistribution.cumulativeProbability(x);
	}
	
	@Override
	public boolean calcIsProcessIsAble(double cp, double cpk, double pp, double ppk) {
		return cp >= 1.0 && cpk >= 1.0 && pp >=1.0 && ppk >= 1.0;
	}
	
}
