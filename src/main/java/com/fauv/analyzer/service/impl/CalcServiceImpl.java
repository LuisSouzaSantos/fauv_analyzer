package com.fauv.analyzer.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fauv.analyzer.entity.MeasurementAxisCoordinate;
import com.fauv.analyzer.entity.MeasurementFm;
import com.fauv.analyzer.entity.MeasurementPmp;
import com.fauv.analyzer.entity.NominalAxisCoordinate;
import com.fauv.analyzer.entity.NominalFm;
import com.fauv.analyzer.entity.dto.MeasurementAxisCoordinateDTO;
import com.fauv.analyzer.entity.dto.MeasurementFmDTO;
import com.fauv.analyzer.entity.dto.MeasurementPmpDTO;
import com.fauv.analyzer.entity.indicators.FmIndicator;
import com.fauv.analyzer.entity.indicators.PmpIndicator;
import com.fauv.analyzer.enums.AxisType;
import com.fauv.analyzer.enums.D;
import com.fauv.analyzer.enums.ToleranceTypeStatus;
import com.fauv.analyzer.service.CalcService;

@Service
public class CalcServiceImpl implements CalcService {

	//private static final Logger logger = LoggerFactory.getLogger(CalcServiceImpl.class);
    private static final DecimalFormat FM_FORMAT_CALCULATE = new DecimalFormat("#.#");
    //private static final DecimalFormat PMP_FORMAT_CALCULATE = new DecimalFormat("#.##");
	private static final double BK_PERCENT = 0.75;
	
	@Override
	public FmIndicator calcFmIndicatorUsingDTO(Collection<MeasurementFmDTO> fmMeasurementDTOList) {
		FmIndicator indicator = new FmIndicator();
		
		for (MeasurementFmDTO fmMeasurement : fmMeasurementDTOList) {
			double matValue = Double.parseDouble(FM_FORMAT_CALCULATE.format(fmMeasurement.getValue()-fmMeasurement.getDefaultValue()));
			
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
			
			matValue = Double.parseDouble(FM_FORMAT_CALCULATE.format(matValue));
			
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
		
		if (isBk) { return ToleranceTypeStatus.BK; }
		
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
				return value > lowerTolerance || value < higherTolerance;
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
				return (value >= lowerTolerance && value<= lowerToleranceBkTolerance);
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
		return (lsc-lic)/(6*(avgMovelRange/d2));
	}
	
	public double calcCpk(double lsc, double lic, double avgMat, double avgMovelRange, double d2) {
		double cpi = (avgMat-lic)/(3*(avgMovelRange*d2));
		double cps = (lsc-avgMat)/(3*(avgMovelRange*d2));
		
		return Double.min(cpi, cps);
	}
	
	public double calcSigmaLevel(double cpk) {
		return 3*cpk;
	}
	
	public double calcPp(double lsc, double lic, double startandDeviation) {
		return (lsc-lic)/(6*startandDeviation);
	}
	
	public double calcPpk(double lsc, double lic, double avgMat, double startandDeviation) {		
		double ppi = (avgMat-lic)/(3*startandDeviation);
		double pps = (lsc-avgMat)/(3*startandDeviation);
		
		return Double.min(ppi, pps);
	}
	
	public double calcNominalDistributionZ1(double lsc, double avgMat, double standardDeviation, int numberOfSamples) {				
		return normDist(lsc, avgMat, standardDeviation, true);
	}

	public double calcNominalDistributionZ2(double lic, double avgMat, double standardDeviation, int numberOfSamples) {
	    return normDist(lic, avgMat, standardDeviation, true);
	}
	
    public static double normDist(double x, double mean, double standardDev, boolean cumulative) {
        if (cumulative) {
            // return the cumulative distribution function
            return (1.0 + erf((x - mean) / (standardDev * Math.sqrt(2.0)))) / 2.0;
        } else {
            // return the probability density function
            return Math.exp(-(x - mean) * (x - mean) / (2.0 * standardDev * standardDev))
                    / (standardDev * Math.sqrt(2.0 * Math.PI));
        }
    }
    
    private static double erf(double x) {
        double t = 1.0 / (1.0 + 0.5 * Math.abs(x));
        double ans = 1 - t * Math.exp(-x*x - 1.26551223 + t * (1.00002368 + t * (0.37409196 + t * (0.09678418 + t * (-0.18628806 + t * (0.27886807 + t * (-1.13520398 + t * (1.48851587 + t * (-0.82215223 + t * 0.17087277)))))))));
        if (x >= 0.0) {
            return ans;
        } else {
            return -ans;
        }
    }
	
}
