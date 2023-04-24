package com.fauv.analyzer.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fauv.analyzer.entity.MeasurementFm;
import com.fauv.analyzer.entity.Model;
import com.fauv.analyzer.entity.NominalFm;
import com.fauv.analyzer.entity.dto.FmDTO;
import com.fauv.analyzer.entity.dto.ModelDTO;
import com.fauv.analyzer.entity.dto.PmpDTO;
import com.fauv.analyzer.entity.dto.SampleDTO;
import com.fauv.analyzer.entity.indicators.FmIndicator;
import com.fauv.analyzer.entity.statistics.CepIndividualValuesGraphic;
import com.fauv.analyzer.entity.statistics.CepMovelAmplitudeGraphic;
import com.fauv.analyzer.entity.statistics.FmStatistic;
import com.fauv.analyzer.entity.statistics.IndividualValuesGraphic;
import com.fauv.analyzer.entity.statistics.MovelAmplitudeGraphic;
import com.fauv.analyzer.entity.statistics.Statistic;
import com.fauv.analyzer.enums.D;
import com.fauv.analyzer.service.CalcService;
import com.fauv.analyzer.service.GraphicService;
import com.fauv.analyzer.service.ModelService;
import com.fauv.analyzer.service.SampleService;
import com.fauv.analyzer.service.StatisticService;
import com.fauv.analyzer.utils.Utils;

@Service
public class StatisticServiceImpl implements StatisticService {
	
    private static final DecimalFormat FM_FORMAT_CALCULATE = new DecimalFormat("#.#");
    
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private ModelService modelService;
	
	@Autowired
	private CalcService calcService;
	
	@Autowired
	private GraphicService graphicService;

	@Override
	public List<Statistic> getAll(Long unitId) {
		Set<Model> models = modelService.getAllModelsByUnitId(unitId);
		Set<SampleDTO> samples = sampleService.getByModels(models);
		
		List<Statistic> statistics = new ArrayList<>();
		
		for (SampleDTO sampleDTO : samples) {
			Statistic foundStatistic = statistics.stream()
					.filter(statistic -> statistic.getPartNumber().equals(sampleDTO.getModel().getPartNumber()))
					.findFirst().orElse(null);
			
			if (foundStatistic == null) {
				ModelDTO model = sampleDTO.getModel();
				
				foundStatistic = new Statistic();
				foundStatistic.setUnitName(sampleDTO.getModel().getCar().getUnit().getName());
				foundStatistic.setCarName(sampleDTO.getModel().getCar().getName());
				foundStatistic.setPartNumber(sampleDTO.getModel().getPartNumber());
				foundStatistic.setModelId(model.getId());
				
				List<PmpDTO> pmpList = model.getPmpList().stream().map(pmp -> {
					PmpDTO pmpDTO = new PmpDTO();
					
					pmpDTO.setName(pmp.getName());
					
					return pmpDTO;
				}).collect(Collectors.toList());
				
				List<FmDTO> fmList = model.getFmList().stream().map(fm -> {
					FmDTO fmDTO = new FmDTO();
					
					fmDTO.setAxis(fm.getAxis());
					fmDTO.setName(fm.getName());
					fmDTO.setLevel(fm.getLevel());
					
					return fmDTO;
				}).collect(Collectors.toList());
				
				foundStatistic.setDefaultFmNames(fmList);
				foundStatistic.setDefaultPmpNames(pmpList);
				
				statistics.add(foundStatistic);
			}
			
			foundStatistic.setInitDate(sampleDTO.getScanInitDate().toLocalDate());
			foundStatistic.setEndDate(sampleDTO.getScanEndDate().toLocalDate());
			foundStatistic.setTotalAk(foundStatistic.getTotalAk()+sampleDTO.getFmIndicator().getAk());
			foundStatistic.setTotalBk(foundStatistic.getTotalBk()+sampleDTO.getFmIndicator().getBk());
			foundStatistic.setTotalIo(foundStatistic.getTotalIo()+sampleDTO.getFmIndicator().getIo());
			foundStatistic.setNumberOfSamples(foundStatistic.getNumberOfSamples()+1);
			foundStatistic.getSamplesIds().add(sampleDTO.getId());
		} 
		
		return statistics;
	}
	
	@Override
	public FmStatistic generateFmStatistic(Long modelId, String fmName) {		
		Model model = modelService.getById(modelId);
		
		List<MeasurementFm> measurementFmList = sampleService.getMeasurementFmBasedOnModelAndFmName(model, fmName);
		
		return buildFmStatistic(measurementFmList);
	}
	
	private FmStatistic buildFmStatistic(List<MeasurementFm> measurementFmList) {
		FmStatistic fmStatistic = new FmStatistic();
		
		List<Double> fmValues = measurementFmList.stream().map(measurementFm -> measurementFm.getValue().doubleValue()).collect(Collectors.toList());
		
		int numberOfSamples = fmValues.size();
		double d2 = D.D2.getConstant(numberOfSamples);
		double d3 = D.D3.getConstant(numberOfSamples);
		double d4 = D.D4.getConstant(numberOfSamples);
		
		NominalFm nominalFm = getNominalFm(measurementFmList);
		
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
		//double nominalDistribution = calcService.calcNominalDistribution(higherTolerance, lowerTolerance, avgMat, startandDeviation, numberOfSamples);
		FmIndicator fmIndicator = calcService.calcFmIndicator(measurementFmList);
		
		IndividualValuesGraphic individualValuesGraphic = graphicService.buildIndividualGraphic(higherTolerance, lowerTolerance, avgMat, measurementFmList, mat);
		MovelAmplitudeGraphic movelAmplitudeGraphic = graphicService.buildMovelAmplideGraphic(1.0, 0.0, averageLineMovelAmplitude, measurementFmList, movelRange);
		CepMovelAmplitudeGraphic cepMovelAmplitudeGraphic = graphicService.buildCepMovelAmplitudeGraphic(lcsMovelAmplitude, licMovelAmplitude, averageLineMovelAmplitude, measurementFmList, movelRange);
		CepIndividualValuesGraphic cepIndividualValuesGraphic = graphicService.buildCepIndividualValuesGraphic(lscCep, licCep, averageLine, avgMat, valueToCalcateZone, measurementFmList);
		
		fmStatistic.setCatalogType(nominalFm.getCatalogType());
		fmStatistic.setName(nominalFm.getName());
		fmStatistic.setIndividualValuesGraphic(individualValuesGraphic);
		fmStatistic.setMovelAmplitudeGraphic(movelAmplitudeGraphic);
		fmStatistic.setCepIndividualValuesGraphic(cepIndividualValuesGraphic);
		fmStatistic.setCepMovelAmplitudeGraphic(cepMovelAmplitudeGraphic);
		fmStatistic.setStandardDeviation(startandDeviation);
		fmStatistic.setAverage(avgMat);
		fmStatistic.setCp(cp);
		fmStatistic.setCpk(cpk);
		fmStatistic.setSigmaLevel(sigmaLevel);
		fmStatistic.setPp(pp);
		fmStatistic.setPpk(ppk);
		fmStatistic.setTotalAk(fmIndicator.getAk());
		fmStatistic.setTotalBk(fmIndicator.getBk());
		fmStatistic.setTotalIo(fmIndicator.getIo());
		fmStatistic.setMappedPmpList(nominalFm.getPointsUsingToMap().stream().map(pmp -> pmp.getName()).collect(Collectors.toList()));
		fmStatistic.setImpactList(nominalFm.getFmImpactList().stream().map(impact -> impact.getInfo()).collect(Collectors.toList()));
		//fmStatistic.setNominalDistribution(nominalDistribution);
		
		return fmStatistic;
	}
		
	private NominalFm getNominalFm(List<MeasurementFm> measurementFmList) {
		if (measurementFmList == null || measurementFmList.isEmpty()) { return null; }
		
		MeasurementFm firstMeasurementFm = measurementFmList.get(0);

		return firstMeasurementFm.getNominalFm();
	}
	
	private List<Double> mapMatUsingFmValues(List<Double> fmListValues, double defaultValue) {
		return fmListValues.stream().map(value -> value-defaultValue).collect(Collectors.toList());
	}

}
