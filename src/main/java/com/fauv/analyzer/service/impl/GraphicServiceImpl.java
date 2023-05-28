package com.fauv.analyzer.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fauv.analyzer.entity.MeasurementAxisCoordinate;
import com.fauv.analyzer.entity.MeasurementFm;
import com.fauv.analyzer.entity.Sample;
import com.fauv.analyzer.entity.statistics.CepIndividualValuesGraphic;
import com.fauv.analyzer.entity.statistics.CepMovelAmplitudeGraphic;
import com.fauv.analyzer.entity.statistics.DetailedAxisCoordinateGraphicHelper;
import com.fauv.analyzer.entity.statistics.DetailedGraphic;
import com.fauv.analyzer.entity.statistics.DetailedFmGraphicHelper;
import com.fauv.analyzer.entity.statistics.IndividualValuesGraphic;
import com.fauv.analyzer.entity.statistics.MovelAmplitudeGraphic;
import com.fauv.analyzer.enums.GraphicType;
import com.fauv.analyzer.enums.StatisticCriteria;
import com.fauv.analyzer.service.GraphicService;
import com.fauv.analyzer.utils.Utils;

@Service
public class GraphicServiceImpl implements GraphicService {

	@Override
	public CepIndividualValuesGraphic buildCepIndividualValuesGraphic(double higherTolerance, double lowerTolerance,
			double midline, double avgMat, double valueToCalcateZone, List<MeasurementFm> measurementFmList) {
		CepIndividualValuesGraphic graphic = new CepIndividualValuesGraphic();
		
		graphic.setPositiveZoneA(Utils.formatNumberToFmGraphic(avgMat+3*valueToCalcateZone));
		graphic.setPositiveZoneB(Utils.formatNumberToFmGraphic(avgMat+2*valueToCalcateZone));
		graphic.setPositiveZoneC(Utils.formatNumberToFmGraphic(avgMat+1*valueToCalcateZone));
		graphic.setNegativeZoneA(Utils.formatNumberToFmGraphic(avgMat-3*valueToCalcateZone));
		graphic.setNegativeZoneB(Utils.formatNumberToFmGraphic(avgMat-2*valueToCalcateZone));
		graphic.setNegativeZoneC(Utils.formatNumberToFmGraphic(avgMat-1*valueToCalcateZone));
		graphic.setHigherTolerance(Utils.formatNumberToFmGraphic(higherTolerance));
		graphic.setLowerTolerance(Utils.formatNumberToFmGraphic(lowerTolerance));
		graphic.setMidline(Utils.formatNumberToFmGraphic(midline));
		graphic.setGraphicType(GraphicType.CEP_INDIVIDUAL_VALUES);
		
		List<DetailedFmGraphicHelper> detailedFmGraphicHelper = measurementFmList.stream().map(fm -> { 
				Sample sample = fm.getSample();
				DetailedGraphic detailedFmGraphic = new DetailedGraphic(sample.getId(), sample.getPin(), sample.getScanEndDate(), Utils.formatNumberToFmGraphic((fm.getValue().doubleValue()-fm.getNominalFm().getDefaultValue().doubleValue())));
				
				return new DetailedFmGraphicHelper(detailedFmGraphic, fm);
		}).collect(Collectors.toList());
		
		includeOutsideControlLimitsIfNeeded(higherTolerance, lowerTolerance, detailedFmGraphicHelper);
		includeTwoOrMoreRunsOfThreeConsecutivePointsCauseIfNeeded(graphic.getPositiveZoneA(), graphic.getPositiveZoneB(), graphic.getNegativeZoneA(), graphic.getNegativeZoneB(), detailedFmGraphicHelper);
		includeFifteenConsecutivePointsInZoneCCauseIfNeeded(graphic.getPositiveZoneC(), graphic.getNegativeZoneC(), midline, detailedFmGraphicHelper);
		includeFourOrMorePointsOfFiveConsecutivePointsOnSameSideOfMeanCauseIfNeeded(graphic.getPositiveZoneC(), graphic.getPositiveZoneA(), graphic.getNegativeZoneC(), graphic.getNegativeZoneA(), detailedFmGraphicHelper);
		includeIfFmIsNineConsecutivePointsInTheSameSideCauseIfNeeded(detailedFmGraphicHelper, higherTolerance, lowerTolerance, midline);
		includeSixConsecutivePointsInAscendingOrDescendingOrderCauseIfNeeded(detailedFmGraphicHelper, higherTolerance, lowerTolerance);
		includeForteenConsecutivePointsAlternatingAboveAndBelowMediumLineCauseIfNeeded(detailedFmGraphicHelper,higherTolerance, lowerTolerance, midline);
		includeEightConsecutivePointsOutOfZoneC(graphic.getPositiveZoneC(), graphic.getNegativeZoneC(),higherTolerance, lowerTolerance, detailedFmGraphicHelper);
		
		graphic.setDetailedFmGraphicsList(detailedFmGraphicHelper.stream().map(detailedFmGraphic -> detailedFmGraphic.getDetailedFmGraphic()).collect(Collectors.toList()));
		
		return graphic;
	}

	@Override
	public CepMovelAmplitudeGraphic buildCepMovelAmplitudeGraphic(double higherTolerance, double lowerTolerance,
			double midline, List<MeasurementFm> measurementFmList, List<Double> movelRange) {
		CepMovelAmplitudeGraphic graphic = new CepMovelAmplitudeGraphic();
		
		graphic.setHigherTolerance(Utils.formatNumberToFmGraphic(higherTolerance));
		graphic.setLowerTolerance(Utils.formatNumberToFmGraphic(lowerTolerance));
		graphic.setMidline(Utils.formatNumberToFmGraphic(midline));
		graphic.setGraphicType(GraphicType.CEP_MOVEL_AMPLITUDE);
		
		List<DetailedFmGraphicHelper> detailedFmGraphicHelper = new ArrayList<>();
		
		for (int i = 0; i < measurementFmList.size(); i++) {
			MeasurementFm measurementFm = measurementFmList.get(i);
			Double movelRangeFm = movelRange.get(i);
			
			Sample sample = measurementFm.getSample();
			
			DetailedGraphic detailedFmGraphic = new DetailedGraphic(sample.getId(), sample.getPin(), sample.getScanEndDate(), Utils.formatNumberToFmGraphic(movelRangeFm));

			detailedFmGraphicHelper.add(new DetailedFmGraphicHelper(detailedFmGraphic, measurementFm));			
		}
		
		includeOutsideControlLimitsIfNeeded(graphic.getHigherTolerance(), graphic.getLowerTolerance(), detailedFmGraphicHelper);
		
		graphic.setDetailedFmGraphicsList(detailedFmGraphicHelper.stream().map(detailedFmGraphic -> detailedFmGraphic.getDetailedFmGraphic()).collect(Collectors.toList()));
		
		return graphic;
	}

	@Override
	public IndividualValuesGraphic buildIndividualGraphic(double higherTolerance, double lowerTolerance, double midline,
			List<MeasurementFm> measurementFmList, List<Double> mat) {
		IndividualValuesGraphic graphic = new IndividualValuesGraphic();
		
		graphic.setHigherTolerance(Utils.formatNumberToFmGraphic(higherTolerance));
		graphic.setLowerTolerance(Utils.formatNumberToFmGraphic(lowerTolerance));
		graphic.setMidline(Utils.formatNumberToFmGraphic(midline));
		graphic.setGraphicType(GraphicType.INDIVIDUAL_VALUES);
		
		List<DetailedFmGraphicHelper> detailedFmGraphicHelper = new ArrayList<>();
		
		for (int i = 0; i < measurementFmList.size(); i++) {
			MeasurementFm measurementFm = measurementFmList.get(i);
			Double singleMatValue = mat.get(i);
			
			Sample sample = measurementFm.getSample();
			
			DetailedGraphic detailedFmGraphic = new DetailedGraphic(sample.getId(), sample.getPin(), sample.getScanEndDate(), Utils.formatNumberToFmGraphic(singleMatValue));
			
			detailedFmGraphicHelper.add(new DetailedFmGraphicHelper(detailedFmGraphic, measurementFm));			
		}
		
		includeOutsideControlLimitsIfNeeded(graphic.getHigherTolerance(), graphic.getLowerTolerance(), detailedFmGraphicHelper);
		
		graphic.setDetailedFmGraphicsList(detailedFmGraphicHelper.stream().map(detailedFmGraphic -> detailedFmGraphic.getDetailedFmGraphic()).collect(Collectors.toList()));
		
		return graphic;
	}

	@Override
	public MovelAmplitudeGraphic buildMovelAmplideGraphic(double higherTolerance, double lowerTolerance, double midline,
			List<MeasurementFm> measurementFmList, List<Double> movelRange) {
		MovelAmplitudeGraphic graphic = new MovelAmplitudeGraphic();
		
		graphic.setHigherTolerance(Utils.formatNumberToFmGraphic(higherTolerance));
		graphic.setLowerTolerance(Utils.formatNumberToFmGraphic(lowerTolerance));
		graphic.setMidline(Utils.formatNumberToFmGraphic(midline));
		graphic.setGraphicType(GraphicType.MOVEL_AMPLITUDE);
		
		List<DetailedFmGraphicHelper> detailedFmGraphicHelper = new ArrayList<>();
		
		for (int i = 0; i < measurementFmList.size(); i++) {
			MeasurementFm measurementFm = measurementFmList.get(i);
			Double movelRangeFm = movelRange.get(i);
			
			Sample sample = measurementFm.getSample();
			
			DetailedGraphic detailedFmGraphic = new DetailedGraphic(sample.getId(), sample.getPin(), sample.getScanEndDate(), Utils.formatNumberToFmGraphic(movelRangeFm));
			
			detailedFmGraphicHelper.add(new DetailedFmGraphicHelper(detailedFmGraphic, measurementFm));			
		}
		
		includeOutsideControlLimitsIfNeeded(graphic.getHigherTolerance(), graphic.getLowerTolerance(), detailedFmGraphicHelper);
		
		graphic.setDetailedFmGraphicsList(detailedFmGraphicHelper.stream().map(detailedFmGraphic -> detailedFmGraphic.getDetailedFmGraphic()).collect(Collectors.toList()));
		
		return graphic;
	}
	
	@Override
	public CepIndividualValuesGraphic buildCepIndividualValuesGraphicAxisCoordinate(double higherTolerance,
			double lowerTolerance, double midline, double avgMat, double valueToCalcateZone,
			List<MeasurementAxisCoordinate> measurementAxisCoordinate) {
		CepIndividualValuesGraphic graphic = new CepIndividualValuesGraphic();
		
		graphic.setPositiveZoneA(Utils.formatNumberToFmGraphic(avgMat+3*valueToCalcateZone));
		graphic.setPositiveZoneB(Utils.formatNumberToFmGraphic(avgMat+2*valueToCalcateZone));
		graphic.setPositiveZoneC(Utils.formatNumberToFmGraphic(avgMat+1*valueToCalcateZone));
		graphic.setNegativeZoneA(Utils.formatNumberToFmGraphic(avgMat-3*valueToCalcateZone));
		graphic.setNegativeZoneB(Utils.formatNumberToFmGraphic(avgMat-2*valueToCalcateZone));
		graphic.setNegativeZoneC(Utils.formatNumberToFmGraphic(avgMat-1*valueToCalcateZone));
		graphic.setHigherTolerance(Utils.formatNumberToFmGraphic(higherTolerance));
		graphic.setLowerTolerance(Utils.formatNumberToFmGraphic(lowerTolerance));
		graphic.setMidline(Utils.formatNumberToFmGraphic(midline));
		graphic.setGraphicType(GraphicType.CEP_INDIVIDUAL_VALUES);
		
		List<DetailedAxisCoordinateGraphicHelper> detailedAxisCoordinateGraphicHelper = measurementAxisCoordinate.stream().map(measurementAxis -> { 
				Sample sample = measurementAxis.getMeasurementPmp().getSample();
				DetailedGraphic detailedFmGraphic = new DetailedGraphic(sample.getId(), sample.getPin(), sample.getScanEndDate(), measurementAxis.getValue().doubleValue());
				
				return new DetailedAxisCoordinateGraphicHelper(detailedFmGraphic, measurementAxis);
		}).collect(Collectors.toList());
		
		includeOutsideControlLimitsIfNeededAxisCoordinate(higherTolerance, lowerTolerance, detailedAxisCoordinateGraphicHelper);
		includeTwoOrMoreRunsOfThreeConsecutivePointsCauseIfNeededAxisCoordinate(graphic.getPositiveZoneA(), graphic.getPositiveZoneB(), graphic.getNegativeZoneA(), graphic.getNegativeZoneB(), detailedAxisCoordinateGraphicHelper);
		includeFifteenConsecutivePointsInZoneCCauseIfNeededAxisCoordinate(graphic.getPositiveZoneC(), graphic.getNegativeZoneC(), midline, detailedAxisCoordinateGraphicHelper);
		includeFourOrMorePointsOfFiveConsecutivePointsOnSameSideOfMeanCauseIfNeededAxisCoordinate(graphic.getPositiveZoneC(), graphic.getPositiveZoneA(), graphic.getNegativeZoneC(), graphic.getNegativeZoneA(), detailedAxisCoordinateGraphicHelper);
		includeIfFmIsNineConsecutivePointsInTheSameSideCauseIfNeededAxisCoordinate(detailedAxisCoordinateGraphicHelper, higherTolerance, lowerTolerance, midline);
		includeSixConsecutivePointsInAscendingOrDescendingOrderCauseIfNeededAxisCoordinate(detailedAxisCoordinateGraphicHelper, higherTolerance, lowerTolerance);
		includeForteenConsecutivePointsAlternatingAboveAndBelowMediumLineCauseIfNeededAxisCoordinate(detailedAxisCoordinateGraphicHelper,higherTolerance, lowerTolerance, midline);
		includeEightConsecutivePointsOutOfZoneCAxisCoordinate(graphic.getPositiveZoneC(), graphic.getNegativeZoneC(),higherTolerance, lowerTolerance, detailedAxisCoordinateGraphicHelper);
		
		graphic.setDetailedFmGraphicsList(detailedAxisCoordinateGraphicHelper.stream().map(detailedGraphic -> detailedGraphic.getDetailedGraphic()).collect(Collectors.toList()));
		
		return graphic;
	}

	@Override
	public CepMovelAmplitudeGraphic buildCepMovelAmplitudeGraphicAxisCoordinate(double higherTolerance,
			double lowerTolerance, double midline, List<MeasurementAxisCoordinate> measurementAxisCoordinateList,
			List<Double> movelRange) {
		CepMovelAmplitudeGraphic graphic = new CepMovelAmplitudeGraphic();
		
		graphic.setHigherTolerance(Utils.formatNumberToFmGraphic(higherTolerance));
		graphic.setLowerTolerance(Utils.formatNumberToFmGraphic(lowerTolerance));
		graphic.setMidline(Utils.formatNumberToFmGraphic(midline));
		graphic.setGraphicType(GraphicType.CEP_MOVEL_AMPLITUDE);
		
		List<DetailedAxisCoordinateGraphicHelper> detailedAxisCoordinateGraphicHelper = new ArrayList<>();
		
		for (int i = 0; i < measurementAxisCoordinateList.size(); i++) {
			MeasurementAxisCoordinate measurementAxisCoordinate = measurementAxisCoordinateList.get(i);
			Double movelRangeFm = movelRange.get(i);
			
			Sample sample = measurementAxisCoordinate.getMeasurementPmp().getSample();
			
			DetailedGraphic detailedGraphic = new DetailedGraphic(sample.getId(), sample.getPin(), sample.getScanEndDate(), Utils.formatNumberToFmGraphic(movelRangeFm));

			detailedAxisCoordinateGraphicHelper.add(new DetailedAxisCoordinateGraphicHelper(detailedGraphic, measurementAxisCoordinate));			
		}
		
		includeOutsideControlLimitsIfNeededAxisCoordinate(graphic.getHigherTolerance(), graphic.getLowerTolerance(), detailedAxisCoordinateGraphicHelper);
		
		graphic.setDetailedFmGraphicsList(detailedAxisCoordinateGraphicHelper.stream().map(detailedGraphic -> detailedGraphic.getDetailedGraphic()).collect(Collectors.toList()));
		
		return graphic;
	}

	@Override
	public IndividualValuesGraphic buildIndividualGraphicAxisCoordinate(double higherTolerance, double lowerTolerance,
			double midline, List<MeasurementAxisCoordinate> measurementAxisCoordinateList, List<Double> mat) {
		IndividualValuesGraphic graphic = new IndividualValuesGraphic();
		
		graphic.setHigherTolerance(Utils.formatNumberToFmGraphic(higherTolerance));
		graphic.setLowerTolerance(Utils.formatNumberToFmGraphic(lowerTolerance));
		graphic.setMidline(Utils.formatNumberToFmGraphic(midline));
		graphic.setGraphicType(GraphicType.INDIVIDUAL_VALUES);
		
		List<DetailedAxisCoordinateGraphicHelper> detailedAxisCoordinateGraphicHelper = new ArrayList<>();
		
		for (int i = 0; i < measurementAxisCoordinateList.size(); i++) {
			MeasurementAxisCoordinate measurementAxisCoordinate = measurementAxisCoordinateList.get(i);
			
			Sample sample = measurementAxisCoordinate.getMeasurementPmp().getSample();
			
			DetailedGraphic detailedGraphic = new DetailedGraphic(sample.getId(), sample.getPin(), sample.getScanEndDate(), measurementAxisCoordinate.getValue().doubleValue());
			
			detailedAxisCoordinateGraphicHelper.add(new DetailedAxisCoordinateGraphicHelper(detailedGraphic, measurementAxisCoordinate));			
		}
		
		includeOutsideControlLimitsIfNeededAxisCoordinate(graphic.getHigherTolerance(), graphic.getLowerTolerance(), detailedAxisCoordinateGraphicHelper);
		
		graphic.setDetailedFmGraphicsList(detailedAxisCoordinateGraphicHelper.stream().map(detailedGraphic -> detailedGraphic.getDetailedGraphic()).collect(Collectors.toList()));
		
		return graphic;
	}

	@Override
	public MovelAmplitudeGraphic buildMovelAmplideGraphicAxisCoordinate(double higherTolerance, double lowerTolerance,
			double midline, List<MeasurementAxisCoordinate> measurementAxisCoordinateList, List<Double> movelRange) {
		MovelAmplitudeGraphic graphic = new MovelAmplitudeGraphic();
		
		graphic.setHigherTolerance(Utils.formatNumberToFmGraphic(higherTolerance));
		graphic.setLowerTolerance(Utils.formatNumberToFmGraphic(lowerTolerance));
		graphic.setMidline(Utils.formatNumberToFmGraphic(midline));
		graphic.setGraphicType(GraphicType.MOVEL_AMPLITUDE);
		
		List<DetailedAxisCoordinateGraphicHelper> detailedAxisCoordinateGraphicHelper = new ArrayList<>();
		
		for (int i = 0; i < measurementAxisCoordinateList.size(); i++) {
			MeasurementAxisCoordinate measurementAxisCoordinate = measurementAxisCoordinateList.get(i);

			Double movelRangeFm = movelRange.get(i);
			
			Sample sample = measurementAxisCoordinate.getMeasurementPmp().getSample();
			
			DetailedGraphic detailedFmGraphic = new DetailedGraphic(sample.getId(), sample.getPin(), sample.getScanEndDate(), Utils.formatNumberToFmGraphic(movelRangeFm));
			
			detailedAxisCoordinateGraphicHelper.add(new DetailedAxisCoordinateGraphicHelper(detailedFmGraphic, measurementAxisCoordinate));			
		}
		
		includeOutsideControlLimitsIfNeededAxisCoordinate(graphic.getHigherTolerance(), graphic.getLowerTolerance(), detailedAxisCoordinateGraphicHelper);
		
		graphic.setDetailedFmGraphicsList(detailedAxisCoordinateGraphicHelper.stream().map(detailedGraphic -> detailedGraphic.getDetailedGraphic()).collect(Collectors.toList()));
		
		return graphic;
	}
	
	
	private void includeOutsideControlLimitsIfNeeded(double higherTolerance, double lowerTolerance, List<DetailedFmGraphicHelper> detailedFmGraphicHelperList) {
		for (DetailedFmGraphicHelper detailedFmGraphicHelper : detailedFmGraphicHelperList) {
			double value = detailedFmGraphicHelper.getDetailedFmGraphic().getValue();
			
			if (value <= higherTolerance && value >= lowerTolerance) { continue; }
			
			detailedFmGraphicHelper.getDetailedFmGraphic().getStatisticCriteriaList().add(StatisticCriteria.OUT_OF_TOLERANCE);
		}
	}
	
	private void includeOutsideControlLimitsIfNeededAxisCoordinate(double higherTolerance, double lowerTolerance, List<DetailedAxisCoordinateGraphicHelper> detailedFmGraphic) {
		for (DetailedAxisCoordinateGraphicHelper detailedAxisCoordinateGraphicHelper : detailedFmGraphic) {
			double value = detailedAxisCoordinateGraphicHelper.getDetailedGraphic().getValue();
			
			if (value <= higherTolerance && value >= lowerTolerance) { continue; }
			
			detailedAxisCoordinateGraphicHelper.getDetailedGraphic().getStatisticCriteriaList().add(StatisticCriteria.OUT_OF_TOLERANCE);
		}
	}
	
	private void includeFifteenConsecutivePointsInZoneCCauseIfNeeded(double positiveZoneC, double negativeZoneC, double midline,
			List<DetailedFmGraphicHelper> detailedFmGraphicHelperList) {
		if (detailedFmGraphicHelperList.size() < 15) { return; }
			    
	    int positiveZone = 0;
	    int negativeZone = 0;
	    
	    for (DetailedFmGraphicHelper detailedFmGraphicHelper : detailedFmGraphicHelperList) {
	    	double value = detailedFmGraphicHelper.getDetailedFmGraphic().getValue();
	    	
		    if (isInsideTwoLimitsValues(value, positiveZoneC, midline)) { 
		    	positiveZone++;
		    	negativeZone = 0;
		    }
		    else if (isInsideTwoLimitsValues(value, midline, negativeZoneC)) { 
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
	
	private void includeFifteenConsecutivePointsInZoneCCauseIfNeededAxisCoordinate(double positiveZoneC, double negativeZoneC, double midline,
			List<DetailedAxisCoordinateGraphicHelper> detailedGraphic) {
		if (detailedGraphic.size() < 15) { return; }
			    
	    int positiveZone = 0;
	    int negativeZone = 0;
	    
	    for (DetailedAxisCoordinateGraphicHelper detailedAxisCoordinateGraphicHelper : detailedGraphic) {
	    	double value = detailedAxisCoordinateGraphicHelper.getDetailedGraphic().getValue();
	    	
		    if (isInsideTwoLimitsValues(value, positiveZoneC, midline)) { 
		    	positiveZone++;
		    	negativeZone = 0;
		    }
		    else if (isInsideTwoLimitsValues(value, midline, negativeZoneC)) { 
		    	negativeZone++;
		    	positiveZone = 0;
		    }else {
		    	positiveZone = 0;
			    negativeZone = 0;
		    }
		    
		    if (positiveZone >= 15 || negativeZone >= 15) {
		    	detailedAxisCoordinateGraphicHelper.getDetailedGraphic().getStatisticCriteriaList().add(StatisticCriteria.FIFTEEN_CONSECUTIVE_POINS_IN_ZONE_C);
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
	    		    	
		    if (isInsideTwoLimitsValues(value, positiveZoneAUpper, positiveZoneBUpper)) { 
		    	positiveZone++;
		    	negativeZone = 0;
		    }
		    else if (isInsideTwoLimitsValues(value, negativeZoneAUpper, negativeZoneBDown)) { 
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
	
	private void includeTwoOrMoreRunsOfThreeConsecutivePointsCauseIfNeededAxisCoordinate(double positiveZoneAUpper, double positiveZoneBUpper,
			double negativeZoneAUpper, double negativeZoneBDown, List<DetailedAxisCoordinateGraphicHelper> detailedGraphic) {
		if (detailedGraphic.size() < 3) { return ; }
			    
	    int positiveZone = 0;
	    int negativeZone = 0;
	    
	    for (DetailedAxisCoordinateGraphicHelper detailedAxisCoordinateGraphicHelper : detailedGraphic) {
	    	double value = detailedAxisCoordinateGraphicHelper.getDetailedGraphic().getValue();
	    		    	
		    if (isInsideTwoLimitsValues(value, positiveZoneAUpper, positiveZoneBUpper)) { 
		    	positiveZone++;
		    	negativeZone = 0;
		    }
		    else if (isInsideTwoLimitsValues(value, negativeZoneAUpper, negativeZoneBDown)) { 
		    	negativeZone++; 
		    	positiveZone = 0;
		    }else {
		    	positiveZone = 0;
			    negativeZone = 0;
		    }
		    
		    if (positiveZone >= 2 || negativeZone >= 2) {
		    	detailedAxisCoordinateGraphicHelper.getDetailedGraphic().getStatisticCriteriaList().add(StatisticCriteria.TWO_OUT_OF_THREE_CONSECUTIVE_POINTS_IN_ZONE_A);
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
	    	
	    	if (isInsideTwoLimitsValues(value, positiveZoneA, positiveZoneC)) { 
		    	positiveZone++;
		    	negativeZone = 0;
		    }
		    else if (isInsideTwoLimitsValues(value, negativeZoneC, negativeZoneA)) { 
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
	
	private void includeFourOrMorePointsOfFiveConsecutivePointsOnSameSideOfMeanCauseIfNeededAxisCoordinate(double positiveZoneC, double positiveZoneA,
			double negativeZoneC, double negativeZoneA, List<DetailedAxisCoordinateGraphicHelper> detailedGraphic) {
		if (detailedGraphic.size() < 3) { return ; }
			    
	    int positiveZone = 0;
	    int negativeZone = 0;
	    
	    for (DetailedAxisCoordinateGraphicHelper detailedAxisCoordinateGraphicHelper : detailedGraphic) {
	    	double value = detailedAxisCoordinateGraphicHelper.getDetailedGraphic().getValue();
	    	
	    	if (isInsideTwoLimitsValues(value, positiveZoneA, positiveZoneC)) { 
		    	positiveZone++;
		    	negativeZone = 0;
		    }
		    else if (isInsideTwoLimitsValues(value, negativeZoneC, negativeZoneA)) { 
		    	negativeZone++;
		    	positiveZone = 0;
		    }else {
		    	positiveZone = 0;
			    negativeZone = 0;
		    }
		    
		    if (positiveZone >= 4 || negativeZone >= 4) {
		    	detailedAxisCoordinateGraphicHelper.getDetailedGraphic().getStatisticCriteriaList().add(StatisticCriteria.FOUR_OUT_OF_FIVE_CONSECUTIVE_POINTS_OUTSIDE_ZONE_C);
		    }
	    	
		}
	    		
	}
	
	private void includeIfFmIsNineConsecutivePointsInTheSameSideCauseIfNeeded(List<DetailedFmGraphicHelper> detailedFmGraphicHelperList,  double lcs, double lic, double midline) {
		if (detailedFmGraphicHelperList.size() <= 8) { return; }
				
		boolean isCountGreatherThanMediumValue = true;
		int count = 0;
		
		for (DetailedFmGraphicHelper detailedFmGraphicHelper : detailedFmGraphicHelperList) {
			double value = detailedFmGraphicHelper.getDetailedFmGraphic().getValue();
			
			boolean valueIsGratherThanMediumLine = value > midline;
			boolean valueIsSmallerThanMidline = value < midline;
			
			if (!isInsideTwoLimitsValues(value, lcs, lic)) { count = 0; }
			else if (isCountGreatherThanMediumValue && valueIsSmallerThanMidline) { count = 0; }
			else if (!isCountGreatherThanMediumValue && valueIsGratherThanMediumLine) { count = 0; }
			else if (!valueIsGratherThanMediumLine && !valueIsSmallerThanMidline) { count = 0; }
			else { count++; }
			
			if (count >= 9) {
				detailedFmGraphicHelper.getDetailedFmGraphic().getStatisticCriteriaList().add(StatisticCriteria.NINE_CONSECUTIVE_POINTS_ON_THE_SAME_SIDE_OF_THE_MIDDLE_LINE);
			}
		}
		
	}
	
	private void includeIfFmIsNineConsecutivePointsInTheSameSideCauseIfNeededAxisCoordinate(List<DetailedAxisCoordinateGraphicHelper> detailedGraphic,  double lcs, double lic, double midline) {
		if (detailedGraphic.size() <= 8) { return; }
				
		boolean isCountGreatherThanMediumValue = true;
		int count = 0;
		
		for (DetailedAxisCoordinateGraphicHelper detailedAxisCoordinateGraphicHelper : detailedGraphic) {
			double value = detailedAxisCoordinateGraphicHelper.getDetailedGraphic().getValue();
			
			boolean valueIsGratherThanMediumLine = value > midline;
			boolean valueIsSmallerThanMidline = value < midline;
			
			if (!isInsideTwoLimitsValues(value, lcs, lic)) { count = 0; }
			else if (isCountGreatherThanMediumValue && valueIsSmallerThanMidline) { count = 0; }
			else if (!isCountGreatherThanMediumValue && valueIsGratherThanMediumLine) { count = 0; }
			else if (!valueIsGratherThanMediumLine && !valueIsSmallerThanMidline) { count = 0; }
			else { count++; }
			
			if (count >= 9) {
				detailedAxisCoordinateGraphicHelper.getDetailedGraphic().getStatisticCriteriaList().add(StatisticCriteria.NINE_CONSECUTIVE_POINTS_ON_THE_SAME_SIDE_OF_THE_MIDDLE_LINE);
			}
		}
		
	}
	
	private void includeSixConsecutivePointsInAscendingOrDescendingOrderCauseIfNeeded(List<DetailedFmGraphicHelper> detailedFmGraphicHelperList, double lcs, double lic) {
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

	        if ((!isInsideTwoLimitsValues(currentValue, lcs, lic)) || (!isAscending && !isDescending)) { 
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
	
	private void includeSixConsecutivePointsInAscendingOrDescendingOrderCauseIfNeededAxisCoordinate(List<DetailedAxisCoordinateGraphicHelper> detailedGraphic, double lcs, double lic) {
	    if (detailedGraphic.size() < 6) { return; }
	    	    
	    int consecutivePointsAsc = 0;
	    int consecutivePointsDes = 0;
	    boolean isAscending = false;
	    boolean isDescending = false;
	    
	    double previousValue = detailedGraphic.get(0).getDetailedGraphic().getValue();
	    
	    for (int i = 1; i < detailedGraphic.size(); i++) {
	    	DetailedAxisCoordinateGraphicHelper detailedAxisCoordinateGraphicHelper = detailedGraphic.get(i);
	    	
	        double currentValue = detailedAxisCoordinateGraphicHelper.getDetailedGraphic().getValue();
	        
	        isAscending = currentValue > previousValue;
	        isDescending = currentValue < previousValue;

	        if ((!isInsideTwoLimitsValues(currentValue, lcs, lic)) || (!isAscending && !isDescending)) { 
	        	consecutivePointsAsc = 0; 
	        	consecutivePointsDes = 0; 
	        	continue;
	        }
	        
	        if (isAscending) { consecutivePointsAsc++; consecutivePointsDes=1; }
	        if (isDescending) { consecutivePointsDes++; consecutivePointsAsc=1; }
	        
	        if (consecutivePointsAsc >= 6) { 
	        	detailedAxisCoordinateGraphicHelper.getDetailedGraphic().getStatisticCriteriaList().add(StatisticCriteria.SIX_CONSECUTIVE_POINTS_ASCENDING);
	        }
	        
	        if (consecutivePointsDes >= 6) { 
	        	detailedAxisCoordinateGraphicHelper.getDetailedGraphic().getStatisticCriteriaList().add(StatisticCriteria.SIX_CONSECUTIVE_POINTS_DESCENDING);
	        }

	        previousValue = currentValue;
	    }

	}
	
	private void includeForteenConsecutivePointsAlternatingAboveAndBelowMediumLineCauseIfNeeded(List<DetailedFmGraphicHelper> detailedFmGraphicHelperList, double lcs, double lic, double mediumLine) {
	    if (detailedFmGraphicHelperList.size() < 14) { return; }
	    
	    int consecutivePointsCount = 0;
	    
	    DetailedFmGraphicHelper firstDetailedFmGraphic = detailedFmGraphicHelperList.get(0);
	    
	    boolean isAboveMean = firstDetailedFmGraphic.getDetailedFmGraphic().getValue() > mediumLine;
	    boolean isBelowMean = firstDetailedFmGraphic.getDetailedFmGraphic().getValue() < mediumLine;
	    
	    for (int i = 0; i < detailedFmGraphicHelperList.size(); i++) {
	    	DetailedFmGraphicHelper detailedFmGraphicHelper = detailedFmGraphicHelperList.get(i);
	    	
	    	double value = detailedFmGraphicHelper.getDetailedFmGraphic().getValue();
	    	
	        if (!isInsideTwoLimitsValues(value, lcs, lic)) {
	        	consecutivePointsCount = 0;
	        	continue;
	        }
	        
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
	
	private void includeForteenConsecutivePointsAlternatingAboveAndBelowMediumLineCauseIfNeededAxisCoordinate(List<DetailedAxisCoordinateGraphicHelper> detailedGraphic, double lcs, double lic, double mediumLine) {
	    if (detailedGraphic.size() < 14) { return; }
	    
	    int consecutivePointsCount = 0;
	    
	    DetailedAxisCoordinateGraphicHelper firstDetailedAxisCoordinateGraphicHelper = detailedGraphic.get(0);
	    
	    boolean isAboveMean = firstDetailedAxisCoordinateGraphicHelper.getDetailedGraphic().getValue() > mediumLine;
	    boolean isBelowMean = firstDetailedAxisCoordinateGraphicHelper.getDetailedGraphic().getValue() < mediumLine;
	    
	    for (int i = 0; i < detailedGraphic.size(); i++) {
	    	DetailedAxisCoordinateGraphicHelper detailedAxisCoordinateGraphicHelper = detailedGraphic.get(i);
	    	
	    	double value = detailedAxisCoordinateGraphicHelper.getDetailedGraphic().getValue();
	    	
	        if (!isInsideTwoLimitsValues(value, lcs, lic)) {
	        	consecutivePointsCount = 0;
	        	continue;
	        }
	        
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
	        	detailedAxisCoordinateGraphicHelper.getDetailedGraphic().getStatisticCriteriaList().add(StatisticCriteria.FOURTEEN_CONSECUTIVE_POINTS_ALTERNATING_UP_AND_DOWN);
	        }
	       
	    }
	}
	
	public void includeEightConsecutivePointsOutOfZoneC(double positiveZoneC, double negativeZoneC, double lcs, double lic, List<DetailedFmGraphicHelper> detailedFmGraphicHelperList) {
		if (detailedFmGraphicHelperList.size() < 8) { return; }
		
		int consecutivePointsCount = 0;
		
		for (int i = 0; i < detailedFmGraphicHelperList.size(); i++) {
	    	DetailedFmGraphicHelper detailedFmGraphicHelper = detailedFmGraphicHelperList.get(i);
	    	
	    	double value = detailedFmGraphicHelper.getDetailedFmGraphic().getValue();
	    	
	        if (!isInsideTwoLimitsValues(value, lcs, lic) || isInsideTwoLimitsValues(value, positiveZoneC, negativeZoneC)) {
	        	consecutivePointsCount=0;
	        }else {
	        	consecutivePointsCount++;
	        }
	        
	        if (consecutivePointsCount >= 8) { 
	        	detailedFmGraphicHelper.getDetailedFmGraphic().getStatisticCriteriaList().add(StatisticCriteria.FOUR_OUT_OF_FIVE_CONSECUTIVE_POINTS_OUTSIDE_ZONE_C);
	        }
	        
		}
		
	}
	
	public void includeEightConsecutivePointsOutOfZoneCAxisCoordinate(double positiveZoneC, double negativeZoneC, double lcs, double lic, List<DetailedAxisCoordinateGraphicHelper> detailedGraphic) {
		if (detailedGraphic.size() < 8) { return; }
		
		int consecutivePointsCount = 0;
		
		for (int i = 0; i < detailedGraphic.size(); i++) {
			DetailedAxisCoordinateGraphicHelper detailedAxisCoordinateGraphicHelper = detailedGraphic.get(i);
	    	
	    	double value = detailedAxisCoordinateGraphicHelper.getDetailedGraphic().getValue();
	    	
	        if (!isInsideTwoLimitsValues(value, lcs, lic) || isInsideTwoLimitsValues(value, positiveZoneC, negativeZoneC)) {
	        	consecutivePointsCount=0;
	        }else {
	        	consecutivePointsCount++;
	        }
	        
	        if (consecutivePointsCount >= 8) { 
	        	detailedAxisCoordinateGraphicHelper.getDetailedGraphic().getStatisticCriteriaList().add(StatisticCriteria.FOUR_OUT_OF_FIVE_CONSECUTIVE_POINTS_OUTSIDE_ZONE_C);
	        }
	        
		}
		
	}
	
	public boolean isInsideTwoLimitsValues(double value, double zoneGratherValue, double zoneLowerValue) {
		if (zoneGratherValue < 0.0 && zoneLowerValue < 0.0) {
			return isInsideTwoNegativaZones(value, zoneGratherValue, zoneLowerValue);
		}else if (zoneGratherValue > 0.0 && zoneLowerValue > 0.0) {
			return isInsideTwoPositiveZones(value, zoneGratherValue, zoneLowerValue);
		}else if (zoneGratherValue > 0.0 && zoneLowerValue < 0.0) {
			return isInsideOnePositiveAndOneNegativeZone(value, zoneGratherValue, zoneLowerValue);
		}else if (zoneGratherValue == 0.0 && zoneLowerValue < 0.0) {
			return isInsideZeroAndNegativeZone(value, zoneGratherValue, zoneLowerValue);
		}else if (zoneGratherValue > 0.0 && zoneLowerValue == 0.0) {
			return isInsideZeroAndPositiveZone(value, zoneGratherValue, zoneLowerValue);
		}else {
			System.out.println("NO RULER");
			return false;
		}	
	}

	private boolean isInsideTwoNegativaZones(double value, double zoneGratherValue, double zoneLowerValue) {
		return value > 0.0 ? false : value <= zoneGratherValue && value >= zoneLowerValue;
	}
	
	private boolean isInsideTwoPositiveZones(double value, double zoneGratherValue, double zoneLowerValue) {
		return value > 0.0 ? value<=zoneGratherValue&&value>=zoneLowerValue: false;
	}
	
	private boolean isInsideOnePositiveAndOneNegativeZone(double value, double zoneGratherValue, double zoneLowerValue) {
		return value > 0.0 ? value <= zoneGratherValue : value >= zoneLowerValue;
	}
	
	private boolean isInsideZeroAndNegativeZone(double value, double zoneGratherValue, double zoneLowerValue) {
		return value > 0.0 ? false : value >= zoneLowerValue;
	}
	
	private boolean isInsideZeroAndPositiveZone(double value, double zoneGratherValue, double zoneLowerValue) {
		return value < 0.0 ? false : value <= zoneGratherValue;
	}
		
}

