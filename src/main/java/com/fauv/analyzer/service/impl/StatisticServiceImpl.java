package com.fauv.analyzer.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fauv.analyzer.entity.MeasurementAxisCoordinate;
import com.fauv.analyzer.entity.MeasurementFm;
import com.fauv.analyzer.entity.MeasurementPmp;
import com.fauv.analyzer.entity.Model;
import com.fauv.analyzer.entity.NominalAxisCoordinate;
import com.fauv.analyzer.entity.NominalFm;
import com.fauv.analyzer.entity.NominalPmp;
import com.fauv.analyzer.entity.dto.FmDTO;
import com.fauv.analyzer.entity.dto.PmpDTO;
import com.fauv.analyzer.entity.dto.SampleStatisticsLoadingDTO;
import com.fauv.analyzer.entity.indicators.AxisIndicator;
import com.fauv.analyzer.entity.indicators.FmIndicator;
import com.fauv.analyzer.entity.statistics.CepIndividualValuesGraphic;
import com.fauv.analyzer.entity.statistics.CepMovelAmplitudeGraphic;
import com.fauv.analyzer.entity.statistics.FmStatistic;
import com.fauv.analyzer.entity.statistics.IndividualValuesGraphic;
import com.fauv.analyzer.entity.statistics.MovelAmplitudeGraphic;
import com.fauv.analyzer.entity.statistics.PmpStatistics;
import com.fauv.analyzer.entity.statistics.PmpStatisticsContainer;
import com.fauv.analyzer.entity.statistics.Statistic;
import com.fauv.analyzer.enums.AxisType;
import com.fauv.analyzer.enums.D;
import com.fauv.analyzer.exception.ModelException;
import com.fauv.analyzer.exception.StatisticException;
import com.fauv.analyzer.message.StatisticMessage;
import com.fauv.analyzer.service.CalcService;
import com.fauv.analyzer.service.GraphicService;
import com.fauv.analyzer.service.ModelService;
import com.fauv.analyzer.service.SampleService;
import com.fauv.analyzer.service.StatisticService;
import com.fauv.analyzer.utils.Utils;

@Service
public class StatisticServiceImpl implements StatisticService {
	    
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private ModelService modelService;
	
	@Autowired
	private CalcService calcService;
	
	@Autowired
	private GraphicService graphicService;

	@Override
	public List<Statistic> getAllStatisticsByUnit(Long unitId) {
		Set<Model> models = modelService.getAllModelsByUnitId(unitId);
		
		List<SampleStatisticsLoadingDTO> samplesStatisticsLoading = sampleService.getSampleStatisticsLoadingByModels(models);
		
		List<Statistic> statistics = new ArrayList<>();
		
		for (SampleStatisticsLoadingDTO sample : samplesStatisticsLoading) {
			Statistic foundStatistic = statistics.stream()
					.filter(statistic -> statistic.getPartNumber().equals(sample.getPartNumber()))
					.findFirst().orElse(null);
			
			if (foundStatistic == null) {
				Model selectedModel = models.stream().filter(model -> model.getId().equals(sample.getModelId())).findFirst().orElse(null);
				
				if (selectedModel == null) { continue; }
								
				foundStatistic = new Statistic();
				foundStatistic.setUnitName(sample.getUnitName());
				foundStatistic.setCarName(sample.getCarName());
				foundStatistic.setPartNumber(sample.getPartNumber());
				foundStatistic.setModelId(sample.getModelId());
				
				List<PmpDTO> pmpList = selectedModel.getPmpList().stream().map(pmp -> {
					PmpDTO pmpDTO = new PmpDTO();
					
					pmpDTO.setName(pmp.getName());
					pmpDTO.setActive(pmp.isActive());
					
					return pmpDTO;
				}).collect(Collectors.toList());
				
				List<FmDTO> fmList = selectedModel.getFmList().stream().map(fm -> {
					FmDTO fmDTO = new FmDTO();
					
					fmDTO.setAxis(fm.getAxis());
					fmDTO.setName(fm.getName());
					fmDTO.setLevel(fm.getLevel());
					fmDTO.setActive(fm.isActive());
					
					return fmDTO;
				}).collect(Collectors.toList());
				
				foundStatistic.setDefaultFmNames(fmList);
				foundStatistic.setDefaultPmpNames(pmpList);
				
				statistics.add(foundStatistic);
			}
			
			foundStatistic.setInitDate(sample.getInitDate().toLocalDate());
			foundStatistic.setEndDate(sample.getEndDate().toLocalDate());
			foundStatistic.setTotalAk(foundStatistic.getTotalAk()+sample.getAk());
			foundStatistic.setTotalBk(foundStatistic.getTotalBk()+sample.getBk());
			foundStatistic.setTotalIo(foundStatistic.getTotalIo()+sample.getIo());
			foundStatistic.setNumberOfSamples(foundStatistic.getNumberOfSamples()+1);
			foundStatistic.getSamplesIds().add(sample.getId());
		} 
		
		return statistics;
	}
	
	@Override
	public FmStatistic generateFmStatistic(Long modelId, String fmName) throws ModelException, StatisticException {		
		Model model = modelService.getByIdValidateIt(modelId);
		
		List<MeasurementFm> measurementFmList = sampleService.getMeasurementFmBasedOnModelAndFmName(model, fmName);
		
		if (measurementFmList == null || measurementFmList.isEmpty()) { throw new StatisticException(StatisticMessage.MEASUREMENT_FM_NOT_DATA); }
		if (measurementFmList.size() <= 1) { throw new StatisticException(StatisticMessage.STATISTIC_LESS_THAN_2); }
		
		return buildFmStatistic(measurementFmList);
	}
	
	@Override
	public PmpStatisticsContainer generatePmpStatistic(Long modelId, String pmpName) throws ModelException, StatisticException {
		Model model = modelService.getByIdValidateIt(modelId);
		
		List<MeasurementPmp> measurementPmpList = sampleService.getMeasurementPmpBasedOnModelAndPmpName(model, pmpName);
		
		if (measurementPmpList == null || measurementPmpList.isEmpty()) { throw new StatisticException(StatisticMessage.MEASUREMENT_PMP_NOT_DATA); }
		if (measurementPmpList.size() <= 1) { throw new StatisticException(StatisticMessage.STATISTIC_LESS_THAN_2); }
		
		return buildPmpStatisticsContainer(measurementPmpList, pmpName);
	}
	
	
	private PmpStatisticsContainer buildPmpStatisticsContainer(List<MeasurementPmp> measurementPmpList, String pmpName) {		
		PmpStatisticsContainer pmpStatisticsContainer = new PmpStatisticsContainer();
		pmpStatisticsContainer.setName(pmpName);
		
		List<PmpStatistics> pmpStatisticsList = buildPmpStatistics(measurementPmpList);
		pmpStatisticsContainer.setPmpStatisticsList(pmpStatisticsList);
		pmpStatisticsContainer.setAxisList(pmpStatisticsList.stream().map(pmpStatistics -> pmpStatistics.getAxis()).collect(Collectors.toList()));
		
		return pmpStatisticsContainer;
	}
	
	private List<PmpStatistics> buildPmpStatistics(List<MeasurementPmp> measurementPmpList) {
		List<PmpStatistics> list = new ArrayList<>();
		
		if (measurementPmpList == null || measurementPmpList.isEmpty()) { return list; }
		
		NominalPmp nominalPmp = measurementPmpList.get(0).getNominalPmp();
		
		List<AxisType> axisList = nominalPmp.getAxisCoordinateList().stream().map(axisCoordinate -> axisCoordinate.getAxis()).collect(Collectors.toList());
		
		for (AxisType axis: axisList) {
			PmpStatistics pmpStatistics = new PmpStatistics();
			
			List<MeasurementAxisCoordinate> measurementAxisCoordinateListByAxis = measurementPmpList.stream()
					.map(measurementPmp -> {
						Set<MeasurementAxisCoordinate> measurementAxisCoordinateList = measurementPmp
								.getMeasurementAxisCoordinateList();

						return measurementAxisCoordinateList.stream()
								.filter(measurementAxisCoordinate -> measurementAxisCoordinate.getNominalAxisCoordinate()
										.getAxis().equals(axis))
								.findFirst().orElse(null);
					}).collect(Collectors.toList());
		
			NominalAxisCoordinate nominalAxisCoordinate = measurementAxisCoordinateListByAxis.get(0).getNominalAxisCoordinate();
			
			List<Double> mat = measurementAxisCoordinateListByAxis.stream()
					.map(measurementAxisCoordinate -> measurementAxisCoordinate.getValue().doubleValue())
					.collect(Collectors.toList());
		
			int numberOfSamples = mat.size();
			double d2 = D.D2.getConstant(numberOfSamples);
			double d3 = D.D3.getConstant(numberOfSamples);
			double d4 = D.D4.getConstant(numberOfSamples);
			
			double sumMat = Utils.sumDoubleValueList(mat);
			double avgMat = sumMat/numberOfSamples;
			
			List<Double> movelRange = calcService.calcMovelRange(mat);
			double sumMovelRange = Utils.sumDoubleValueList(movelRange);
			double avgMovelRange = sumMovelRange/(numberOfSamples-1);
			
			double lscCep = calcService.calcLsc(avgMat, avgMovelRange, numberOfSamples);		
			double licCep = calcService.calcLic(avgMat, avgMovelRange, numberOfSamples);
			
			double startandDeviation = calcService.calcStartandDeviation(mat, avgMat, numberOfSamples);
			
			double valueToCalcateZone = avgMovelRange/d2;
					
			double lcsMovelAmplitude = d4*avgMovelRange;
			double licMovelAmplitude = d3*avgMovelRange;
			
			double averageLine = avgMat;
			double averageLineMovelAmplitude = sumMovelRange/(numberOfSamples-1);
			
			double higherTolerance = nominalAxisCoordinate.getHigherTolerance().doubleValue();
			double lowerTolerance = nominalAxisCoordinate.getLowerTolerance().doubleValue();
			
			double cp = calcService.calcCp(higherTolerance, lowerTolerance, avgMovelRange, d2);
			double cpk = calcService.calcCpk(higherTolerance, lowerTolerance, avgMat, avgMovelRange, d2);
			double sigmaLevel = calcService.calcSigmaLevel(cpk);
			double pp = calcService.calcPp(higherTolerance, lowerTolerance, startandDeviation);
			double ppk = calcService.calcPpk(higherTolerance, lowerTolerance, avgMat, startandDeviation);
			
			NormalDistribution normalDistribution = new NormalDistribution(avgMat, startandDeviation);
			double nominalDistribution = normalDistribution.probability(lowerTolerance, higherTolerance);
			double restOfNormalDistribution = 1.0 - nominalDistribution;
			
			boolean isAble = calcService.calcIsProcessIsAble(cp, cpk, pp, ppk);
		
			AxisIndicator axisIndicator = calcService.calcAxisIndicatorOnlyOneAxis(measurementAxisCoordinateListByAxis);
			
			int totalIndicator = axisIndicator.getAk()+axisIndicator.getBk()+axisIndicator.getIo();
			double percentageAk = (double) axisIndicator.getAk()/totalIndicator;
			double percentageBk = (double) axisIndicator.getBk()/totalIndicator;
			double percentageIo = (double) axisIndicator.getIo()/totalIndicator;
			
			IndividualValuesGraphic individualValuesGraphic = graphicService.buildIndividualGraphicAxisCoordinate(higherTolerance, lowerTolerance, avgMat, measurementAxisCoordinateListByAxis, mat);
			MovelAmplitudeGraphic movelAmplitudeGraphic = graphicService.buildMovelAmplideGraphicAxisCoordinate(1.0, 0.0, averageLineMovelAmplitude, measurementAxisCoordinateListByAxis, movelRange);
			CepMovelAmplitudeGraphic cepMovelAmplitudeGraphic = graphicService.buildCepMovelAmplitudeGraphicAxisCoordinate(lcsMovelAmplitude, licMovelAmplitude, averageLineMovelAmplitude, measurementAxisCoordinateListByAxis, movelRange);
			CepIndividualValuesGraphic cepIndividualValuesGraphic = graphicService.buildCepIndividualValuesGraphicAxisCoordinate(lscCep, licCep, averageLine, avgMat, valueToCalcateZone, measurementAxisCoordinateListByAxis);
			
			pmpStatistics.setStandardDeviation(Utils.formatNumberToFmGraphic(startandDeviation));
			pmpStatistics.setAverage(Utils.formatNumberToFmGraphic(avgMat));
			pmpStatistics.setCp(Double.isInfinite(cp) ? Utils.formatNumberToFmGraphic(1.0):  Utils.formatNumberToFmGraphic(cp));
			pmpStatistics.setCpk(Double.isInfinite(cpk) ? Utils.formatNumberToFmGraphic(1.0):  Utils.formatNumberToFmGraphic(cpk));
			pmpStatistics.setSigmaLevel(Double.isInfinite(sigmaLevel) ? Utils.formatNumberToFmGraphic(6.0):  Utils.formatNumberToFmGraphic(sigmaLevel));
			pmpStatistics.setPp(Double.isInfinite(pp) ? Utils.formatNumberToFmGraphic(1.0):  Utils.formatNumberToFmGraphic(pp));
			pmpStatistics.setPpk(Double.isInfinite(ppk) ? Utils.formatNumberToFmGraphic(1.0):  Utils.formatNumberToFmGraphic(ppk));
			pmpStatistics.setTotalAk(axisIndicator.getAk());
			pmpStatistics.setTotalBk(axisIndicator.getBk());
			pmpStatistics.setTotalIo(axisIndicator.getIo());
			pmpStatistics.setPercentageAk(percentageAk);
			pmpStatistics.setPercentageBk(percentageBk);
			pmpStatistics.setPercentageIo(percentageIo);
			pmpStatistics.setNominalDistribution(Utils.formatNomalDistribution(nominalDistribution)*100);
			pmpStatistics.setRestOfNormalDistribution(Utils.formatNomalDistribution(restOfNormalDistribution)*100);
			pmpStatistics.setAble(isAble);
			pmpStatistics.setAxis(axis);
			pmpStatistics.setCepIndividualValuesGraphic(cepIndividualValuesGraphic);
			pmpStatistics.setCepMovelAmplitudeGraphic(cepMovelAmplitudeGraphic);
			pmpStatistics.setIndividualValuesGraphic(individualValuesGraphic);
			pmpStatistics.setMovelAmplitudeGraphic(movelAmplitudeGraphic);
			
			list.add(pmpStatistics);
		}
		
		return list;
	}

	private FmStatistic buildFmStatistic(List<MeasurementFm> measurementFmList) throws StatisticException {		
		NominalFm nominalFm = getFirstNominalFm(measurementFmList);
		
		if (nominalFm == null) { throw new StatisticException(StatisticMessage.NOMINAL_FM_NOT_FOUND); }
		
		List<Double> fmValues = mapDoubleValuesByMeasurementFm(measurementFmList);
		
		int numberOfSamples = fmValues.size();
		
		double d2 = D.D2.getConstant(numberOfSamples);
		double d3 = D.D3.getConstant(numberOfSamples);
		double d4 = D.D4.getConstant(numberOfSamples);
				
		List<Double> mat = mapMatUsingFmValues(fmValues, nominalFm.getDefaultValue().doubleValue());
		
		double sumMat = Utils.sumDoubleValueList(mat);
		double avgMat = sumMat/numberOfSamples;
		
		List<Double> movelRange = calcService.calcMovelRange(mat);
		
		double sumMovelRange = Utils.sumDoubleValueList(movelRange);
		double avgMovelRange = sumMovelRange/(numberOfSamples-1);
		
		double lscCep = calcService.calcLsc(avgMat, avgMovelRange, numberOfSamples);		
		double licCep = calcService.calcLic(avgMat, avgMovelRange, numberOfSamples);
	
		double startandDeviation = calcService.calcStartandDeviation(mat, avgMat, numberOfSamples);
		
		double valueToCalcateZone = avgMovelRange/d2;
				
		double lcsMovelAmplitude = d4*avgMovelRange;
		double licMovelAmplitude = d3*avgMovelRange;
		
		double averageLine = avgMat;
		double averageLineMovelAmplitude = sumMovelRange/(numberOfSamples-1);
		
		double higherTolerance = nominalFm.getHigherTolerance().doubleValue();
		double lowerTolerance = nominalFm.getLowerTolerance().doubleValue();
		
		double cp = calcService.calcCp(higherTolerance, lowerTolerance, avgMovelRange, d2);
		double cpk = calcService.calcCpk(higherTolerance, lowerTolerance, avgMat, avgMovelRange, d2);
		double sigmaLevel = calcService.calcSigmaLevel(cpk);
		double pp = calcService.calcPp(higherTolerance, lowerTolerance, startandDeviation);
		double ppk = calcService.calcPpk(higherTolerance, lowerTolerance, avgMat, startandDeviation);
		
		NormalDistribution normalDistribution = new NormalDistribution(avgMat, startandDeviation);
		double nominalDistribution = normalDistribution.probability(lowerTolerance, higherTolerance);
		double restOfNormalDistribution = 1.0 - nominalDistribution;
		
		boolean isAble = calcService.calcIsProcessIsAble(cp, cpk, pp, ppk);
		
		FmIndicator fmIndicator = calcService.calcFmIndicator(measurementFmList);
		
		IndividualValuesGraphic individualValuesGraphic = graphicService.buildIndividualGraphic(higherTolerance, lowerTolerance, avgMat, measurementFmList, mat);
		MovelAmplitudeGraphic movelAmplitudeGraphic = graphicService.buildMovelAmplideGraphic(1.0, 0.0, averageLineMovelAmplitude, measurementFmList, movelRange);
		CepMovelAmplitudeGraphic cepMovelAmplitudeGraphic = graphicService.buildCepMovelAmplitudeGraphic(lcsMovelAmplitude, licMovelAmplitude, averageLineMovelAmplitude, measurementFmList, movelRange);
		CepIndividualValuesGraphic cepIndividualValuesGraphic = graphicService.buildCepIndividualValuesGraphic(lscCep, licCep, averageLine, avgMat, valueToCalcateZone, measurementFmList);
		
		int totalIndicator = fmIndicator.getAk()+fmIndicator.getBk()+fmIndicator.getIo();
		double percentageAk = (double) fmIndicator.getAk()/totalIndicator;
		double percentageBk = (double) fmIndicator.getBk()/totalIndicator;
		double percentageIo = (double) fmIndicator.getIo()/totalIndicator;
		
		FmStatistic fmStatistic = new FmStatistic();
		fmStatistic.setCatalogType(nominalFm.getCatalogType());
		fmStatistic.setName(nominalFm.getName());
		fmStatistic.setIndividualValuesGraphic(individualValuesGraphic);
		fmStatistic.setMovelAmplitudeGraphic(movelAmplitudeGraphic);
		fmStatistic.setCepIndividualValuesGraphic(cepIndividualValuesGraphic);
		fmStatistic.setCepMovelAmplitudeGraphic(cepMovelAmplitudeGraphic);
		fmStatistic.setStandardDeviation(Utils.formatNumberToFmGraphic(startandDeviation));
		fmStatistic.setAverage(Utils.formatNumberToFmGraphic(avgMat));
		fmStatistic.setCp(Utils.formatNumberToFmGraphic(cp));
		fmStatistic.setCpk(Utils.formatNumberToFmGraphic(cpk));
		fmStatistic.setSigmaLevel(Utils.formatNumberToFmGraphic(sigmaLevel));
		fmStatistic.setPp(Utils.formatNumberToFmGraphic(pp));
		fmStatistic.setPpk(Utils.formatNumberToFmGraphic(ppk));
		fmStatistic.setTotalAk(fmIndicator.getAk());
		fmStatistic.setTotalBk(fmIndicator.getBk());
		fmStatistic.setTotalIo(fmIndicator.getIo());
		fmStatistic.setMappedPmpList(nominalFm.getMappedPmpList());
		fmStatistic.setImpactList(nominalFm.getFmImpactListString());
		fmStatistic.setPercentageAk(percentageAk);
		fmStatistic.setPercentageBk(percentageBk);
		fmStatistic.setPercentageIo(percentageIo);
		fmStatistic.setNominalDistribution(Utils.formatNomalDistribution(nominalDistribution)*100);
		fmStatistic.setRestOfNormalDistribution(Utils.formatNomalDistribution(restOfNormalDistribution)*100);
		fmStatistic.setAble(isAble);
		
		return fmStatistic;
	}
		
	private NominalFm getFirstNominalFm(List<MeasurementFm> measurementFmList) {
		if (measurementFmList == null || measurementFmList.isEmpty()) { return null; }
		
		MeasurementFm firstMeasurementFm = measurementFmList.get(0);

		return firstMeasurementFm.getNominalFm();
	}
	
	private List<Double> mapMatUsingFmValues(List<Double> fmListValues, double defaultValue) {
		return fmListValues.stream().map(value -> value-defaultValue).collect(Collectors.toList());
	}
	
	private List<Double> mapDoubleValuesByMeasurementFm(List<MeasurementFm> measurementFmList) {
		return measurementFmList.stream().map(measurementFm -> measurementFm.getValue().doubleValue()).collect(Collectors.toList());
	}

}
