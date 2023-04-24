package com.fauv.analyzer.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fauv.analyzer.entity.MeasurementFm;
import com.fauv.analyzer.entity.Sample;
import com.fauv.analyzer.entity.statistics.CepIndividualValuesGraphic;
import com.fauv.analyzer.entity.statistics.CepMovelAmplitudeGraphic;
import com.fauv.analyzer.entity.statistics.DetailedFmGraphic;
import com.fauv.analyzer.entity.statistics.DetailedFmGraphicHelper;
import com.fauv.analyzer.entity.statistics.IndividualValuesGraphic;
import com.fauv.analyzer.entity.statistics.MovelAmplitudeGraphic;
import com.fauv.analyzer.enums.GraphicType;
import com.fauv.analyzer.enums.StatisticCriteria;
import com.fauv.analyzer.service.GraphicService;

@Service
public class GraphicServiceImpl implements GraphicService {

	@Override
	public CepIndividualValuesGraphic buildCepIndividualValuesGraphic(double higherTolerance, double lowerTolerance,
			double midline, double avgMat, double valueToCalcateZone, List<MeasurementFm> measurementFmList) {
		CepIndividualValuesGraphic graphic = new CepIndividualValuesGraphic();
		
		graphic.setPositiveZoneA(avgMat+3*valueToCalcateZone);
		graphic.setPositiveZoneB(avgMat+2*valueToCalcateZone);
		graphic.setPosttiveZoneC(avgMat+1*valueToCalcateZone);
		graphic.setNegativeZoneA(avgMat-3*valueToCalcateZone);
		graphic.setNegativeZoneB(avgMat-2*valueToCalcateZone);
		graphic.setNegativeZoneC(avgMat-1*valueToCalcateZone);
		graphic.setHigherTolerance(higherTolerance);
		graphic.setLowerTolerance(lowerTolerance);
		graphic.setMidline(midline);
		graphic.setGraphicType(GraphicType.CEP_INDIVIDUAL_VALUES);
		
		List<DetailedFmGraphicHelper> detailedFmGraphicHelper = measurementFmList.stream().map(fm -> { 
				Sample sample = fm.getSample();
				DetailedFmGraphic detailedFmGraphic = new DetailedFmGraphic(sample.getId(), sample.getPin(), sample.getUploadedDate(), (fm.getValue().doubleValue()-fm.getNominalFm().getDefaultValue().doubleValue()));
				
				return new DetailedFmGraphicHelper(detailedFmGraphic, fm);
		}).collect(Collectors.toList());
		
		includeOutsideControlLimitsIfNeeded(higherTolerance, graphic.getLowerTolerance(), detailedFmGraphicHelper);
		includeTwoOrMoreRunsOfThreeConsecutivePointsCauseIfNeeded(graphic.getPositiveZoneA(), graphic.getPositiveZoneB(), graphic.getNegativeZoneA(), graphic.getNegativeZoneB(), detailedFmGraphicHelper);
		includeFifteenConsecutivePointsInZoneCCauseIfNeeded(graphic.getPosttiveZoneC(), graphic.getNegativeZoneC(), midline, detailedFmGraphicHelper);
		includeFourOrMorePointsOfFiveConsecutivePointsOnSameSideOfMeanCauseIfNeeded(graphic.getPosttiveZoneC(), graphic.getPositiveZoneA(), graphic.getNegativeZoneC(), graphic.getNegativeZoneA(), detailedFmGraphicHelper);
		includeIfFmIsNineConsecutivePointsInTheSameSideCauseIfNeeded(detailedFmGraphicHelper, midline);
		includeSixConsecutivePointsInAscendingOrDescendingOrderCauseIfNeeded(detailedFmGraphicHelper);
		includeForteenConsecutivePointsAlternatingAboveAndBelowMediumLineCauseIfNeeded(detailedFmGraphicHelper, midline);
		
		graphic.setDetailedFmGraphicsList(detailedFmGraphicHelper.stream().map(detailedFmGraphic -> detailedFmGraphic.getDetailedFmGraphic()).collect(Collectors.toList()));
		
		return graphic;
	}

	@Override
	public CepMovelAmplitudeGraphic buildCepMovelAmplitudeGraphic(double higherTolerance, double lowerTolerance,
			double midline, List<MeasurementFm> measurementFmList, List<Double> movelRange) {
		CepMovelAmplitudeGraphic graphic = new CepMovelAmplitudeGraphic();
		
		graphic.setHigherTolerance(higherTolerance);
		graphic.setLowerTolerance(lowerTolerance);
		graphic.setMidline(midline);
		graphic.setGraphicType(GraphicType.CEP_MOVEL_AMPLITUDE);
		
		for (int i = 0; i < measurementFmList.size(); i++) {
			MeasurementFm measurementFm = measurementFmList.get(i);
			Double movelRangeFm = movelRange.get(i);
			
			Sample sample = measurementFm.getSample();
			
			DetailedFmGraphic detailedFmGraphic = new DetailedFmGraphic(sample.getId(), sample.getPin(), sample.getUploadedDate(), movelRangeFm);
			
			graphic.getDetailedFmGraphicsList().add(detailedFmGraphic);
		}
		
		return graphic;
	}

	@Override
	public IndividualValuesGraphic buildIndividualGraphic(double higherTolerance, double lowerTolerance, double midline,
			List<MeasurementFm> measurementFmList, List<Double> mat) {
		IndividualValuesGraphic graphic = new IndividualValuesGraphic();
		
		graphic.setHigherTolerance(higherTolerance);
		graphic.setLowerTolerance(lowerTolerance);
		graphic.setMidline(midline);
		graphic.setGraphicType(GraphicType.INDIVIDUAL_VALUES);
		
		for (int i = 0; i < measurementFmList.size(); i++) {
			MeasurementFm measurementFm = measurementFmList.get(i);
			Double singleMatValue = mat.get(i);
			
			Sample sample = measurementFm.getSample();
			
			DetailedFmGraphic detailedFmGraphic = new DetailedFmGraphic(sample.getId(), sample.getPin(), sample.getUploadedDate(), singleMatValue);
			
			graphic.getDetailedFmGraphicsList().add(detailedFmGraphic);
		}
		
		return graphic;
	}

	@Override
	public MovelAmplitudeGraphic buildMovelAmplideGraphic(double higherTolerance, double lowerTolerance, double midline,
			List<MeasurementFm> measurementFmList, List<Double> movelRange) {
		MovelAmplitudeGraphic graphic = new MovelAmplitudeGraphic();
		
		graphic.setHigherTolerance(higherTolerance);
		graphic.setLowerTolerance(lowerTolerance);
		graphic.setMidline(midline);
		graphic.setGraphicType(GraphicType.MOVEL_AMPLITUDE);
		
		for (int i = 0; i < measurementFmList.size(); i++) {
			MeasurementFm measurementFm = measurementFmList.get(i);
			Double movelRangeFm = movelRange.get(i);
			
			Sample sample = measurementFm.getSample();
			
			DetailedFmGraphic detailedFmGraphic = new DetailedFmGraphic(sample.getId(), sample.getPin(), sample.getUploadedDate(), movelRangeFm);
			
			graphic.getDetailedFmGraphicsList().add(detailedFmGraphic);
		}
		
		return graphic;
	}
	
	
	private void includeOutsideControlLimitsIfNeeded(double higherTolerance, double lowerTolerance, List<DetailedFmGraphicHelper> detailedFmGraphicHelperList) {
		for (DetailedFmGraphicHelper detailedFmGraphicHelper : detailedFmGraphicHelperList) {
			double value = detailedFmGraphicHelper.getDetailedFmGraphic().getValue();
			
			if (value <= higherTolerance && value >= lowerTolerance) { continue; }
			
			detailedFmGraphicHelper.getDetailedFmGraphic().getStatisticCriteriaList().add(StatisticCriteria.OUT_OF_TOLERANCE);
		}
	}
	
	private void includeFifteenConsecutivePointsInZoneCCauseIfNeeded(double positiveZoneC, double negativeZoneC, double midline,
			List<DetailedFmGraphicHelper> detailedFmGraphicHelperList) {
		if (detailedFmGraphicHelperList.size() < 15) { return; }
			    
	    int positiveZone = 0;
	    int negativeZone = 0;
	    
	    for (DetailedFmGraphicHelper detailedFmGraphicHelper : detailedFmGraphicHelperList) {
	    	double value = detailedFmGraphicHelper.getDetailedFmGraphic().getValue();
	    	
		    if (value > midline && value <= positiveZoneC) { 
		    	positiveZone++;
		    	negativeZone = 0;
		    }
		    else if (value < midline && value <= negativeZoneC) { 
		    	negativeZone++;
		    	positiveZone = 0;
		    }else {
		    	positiveZone = 0;
			    negativeZone = 0;
		    }
		    
		    if (positiveZone >= 15 || negativeZone >= 15) {
		    	detailedFmGraphicHelper.getDetailedFmGraphic().getStatisticCriteriaList().add(StatisticCriteria.FIFTEEN_CONSECUTIVE_POINS_IN_ZONE_C);
		    }
	    	
		}
	}
	
	private void includeTwoOrMoreRunsOfThreeConsecutivePointsCauseIfNeeded(double positiveZoneAUpper, double positiveZoneBUpper,
			double negativeZoneAUpper, double negativeZoneBDown, List<DetailedFmGraphicHelper> detailedFmGraphicHelperList) {
		if (detailedFmGraphicHelperList.size() < 3) { return ; }
			    
	    int positiveZone = 0;
	    int negativeZone = 0;
	    
	    for (DetailedFmGraphicHelper detailedFmGraphicHelper : detailedFmGraphicHelperList) {
	    	double value = detailedFmGraphicHelper.getDetailedFmGraphic().getValue();
	    	
		    if (value > positiveZoneBUpper && value <= positiveZoneAUpper) { 
		    	positiveZone++;
		    	negativeZone = 0;
		    }
		    else if (value < negativeZoneBDown && value <= negativeZoneAUpper) { 
		    	negativeZone++; 
		    	positiveZone = 0;
		    }else {
		    	positiveZone = 0;
			    negativeZone = 0;
		    }
		    
		    if (positiveZone >= 2 || negativeZone >= 2) {
		    	detailedFmGraphicHelper.getDetailedFmGraphic().getStatisticCriteriaList().add(StatisticCriteria.TWO_OUT_OF_THREE_CONSECUTIVE_POINTS_IN_ZONE_A);
		    }
	    	
		}
	    		
	}
	
	private void includeFourOrMorePointsOfFiveConsecutivePointsOnSameSideOfMeanCauseIfNeeded(double positiveZoneC, double positiveZoneA,
			double negativeZoneC, double negativeZoneA, List<DetailedFmGraphicHelper> detailedFmGraphicHelperList) {
		if (detailedFmGraphicHelperList.size() < 3) { return ; }
			    
	    int positiveZone = 0;
	    int negativeZone = 0;
	    
	    for (DetailedFmGraphicHelper detailedFmGraphicHelper : detailedFmGraphicHelperList) {
	    	double value = detailedFmGraphicHelper.getDetailedFmGraphic().getValue();
	    	
		    if (value > positiveZoneC && value <= positiveZoneA) { 
		    	positiveZone++;
		    	negativeZone = 0;
		    }
		    else if (value < negativeZoneC && value >= negativeZoneA) { 
		    	negativeZone++;
		    	positiveZone = 0;
		    }else {
		    	positiveZone = 0;
			    negativeZone = 0;
		    }
		    
		    if (positiveZone >= 4 || negativeZone >= 4) {
		    	detailedFmGraphicHelper.getDetailedFmGraphic().getStatisticCriteriaList().add(StatisticCriteria.FOUR_OUT_OF_FIVE_CONSECUTIVE_POINTS_OUTSIDE_ZONE_C);
		    }
	    	
		}
	    		
	}
	
	private void includeIfFmIsNineConsecutivePointsInTheSameSideCauseIfNeeded(List<DetailedFmGraphicHelper> detailedFmGraphicHelperList, double midline) {
		if (detailedFmGraphicHelperList.size() <= 8) { return; }
				
		boolean isCountGreatherThanMediumValue = true;
		int count = 0;
		
		for (DetailedFmGraphicHelper detailedFmGraphicHelper : detailedFmGraphicHelperList) {
			double value = detailedFmGraphicHelper.getDetailedFmGraphic().getValue();
			
			boolean valueIsGratherThanMediumLine = value > midline;
			boolean valueIsSmallerThanMidline = value < midline;
			
			if (isCountGreatherThanMediumValue && valueIsSmallerThanMidline) { count = 0; }
			else if (!isCountGreatherThanMediumValue && valueIsGratherThanMediumLine) { count = 0; }
			else if (!valueIsGratherThanMediumLine && !valueIsSmallerThanMidline) { count = 0; }
			else { count++; }
			
			if (count >= 9) {
				detailedFmGraphicHelper.getDetailedFmGraphic().getStatisticCriteriaList().add(StatisticCriteria.NINE_CONSECUTIVE_POINTS_ON_THE_SAME_SIDE_OF_THE_MIDDLE_LINE);
			}
		}
		
	}
	
	private void includeSixConsecutivePointsInAscendingOrDescendingOrderCauseIfNeeded(List<DetailedFmGraphicHelper> detailedFmGraphicHelperList) {
	    if (detailedFmGraphicHelperList.size() < 6) { return; }
	    	    
	    int consecutivePointsAsc = 0;
	    int consecutivePointsDes = 0;
	    boolean isAscending = false;
	    boolean isDescending = false;
	    
	    double previousValue = detailedFmGraphicHelperList.get(0).getDetailedFmGraphic().getValue();
	    
	    for (int i = 1; i < detailedFmGraphicHelperList.size(); i++) {
	    	DetailedFmGraphicHelper detailedFmGraphicHelper = detailedFmGraphicHelperList.get(i);
	    	
	        double currentValue = detailedFmGraphicHelper.getDetailedFmGraphic().getValue();
	        
	        isAscending = currentValue > previousValue;
	        isDescending = currentValue < previousValue;

	        if (!isAscending && !isDescending) { 
	        	consecutivePointsAsc = 0; 
	        	consecutivePointsDes = 0; 
	        	continue;
	        }
	        
	        if (isAscending) { consecutivePointsAsc++; consecutivePointsDes=1; }
	        if (isDescending) { consecutivePointsDes++; consecutivePointsAsc=1; }
	        
	        if (consecutivePointsAsc >= 6) { 
		    	detailedFmGraphicHelper.getDetailedFmGraphic().getStatisticCriteriaList().add(StatisticCriteria.SIX_CONSECUTIVE_POINTS_ASCENDING);
	        }
	        
	        if (consecutivePointsDes >= 6) { 
		    	detailedFmGraphicHelper.getDetailedFmGraphic().getStatisticCriteriaList().add(StatisticCriteria.SIX_CONSECUTIVE_POINTS_DESCENDING);
	        }

	        previousValue = currentValue;
	    }

	}
	
	private void includeForteenConsecutivePointsAlternatingAboveAndBelowMediumLineCauseIfNeeded(List<DetailedFmGraphicHelper> detailedFmGraphicHelperList, double mediumLine) {
	    if (detailedFmGraphicHelperList.size() < 14) { return; }
	    
	    int consecutivePointsCount = 1;
	    
	    DetailedFmGraphicHelper firstDetailedFmGraphic = detailedFmGraphicHelperList.get(0);
	    
	    boolean isAboveMean = firstDetailedFmGraphic.getDetailedFmGraphic().getValue() > mediumLine;
	    boolean isBelowMean = firstDetailedFmGraphic.getDetailedFmGraphic().getValue() < mediumLine;
	    
	    for (int i = 1; i < detailedFmGraphicHelperList.size(); i++) {
	    	DetailedFmGraphicHelper detailedFmGraphicHelper = detailedFmGraphicHelperList.get(0);
	    	
	    	double value = detailedFmGraphicHelper.getDetailedFmGraphic().getValue();
	    	
	        boolean isCurrentAboveMean = value > mediumLine;
	        boolean isCurrentBelowMean = value < mediumLine;

	        if (isAboveMean && isCurrentBelowMean) { 
	        	consecutivePointsCount++;
	        	isBelowMean = true;
	        	isAboveMean = false;
	        }else if (isBelowMean && isCurrentAboveMean) { 
	        	consecutivePointsCount++;
	        	isBelowMean = false;
	        	isAboveMean = true;
	        }else {
	        	consecutivePointsCount = 0; 
	        }
	            
	        if (consecutivePointsCount >= 14) {
	        	detailedFmGraphicHelper.getDetailedFmGraphic().getStatisticCriteriaList().add(StatisticCriteria.FOURTEEN_CONSECUTIVE_POINTS_ALTERNATING_UP_AND_DOWN);
	        }
	       
	    }
	}

}