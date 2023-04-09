package com.fauv.analyzer.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fauv.analyzer.entity.Model;
import com.fauv.analyzer.entity.dto.SampleDTO;
import com.fauv.analyzer.entity.statistics.Statistic;
import com.fauv.analyzer.service.ModelService;
import com.fauv.analyzer.service.SampleService;
import com.fauv.analyzer.service.StatisticService;

@Service
public class StatisticServiceImpl implements StatisticService {
	
    //private static final DecimalFormat FM_FORMAT_CALCULATE = new DecimalFormat("#.#");
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private ModelService modelService;

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
				foundStatistic = new Statistic();
				foundStatistic.setUnitName(sampleDTO.getModel().getCar().getUnit().getName());
				foundStatistic.setCarName(sampleDTO.getModel().getCar().getName());
				foundStatistic.setPartNumber(sampleDTO.getModel().getPartNumber());				
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
	
//	private boolean calculateLsc(double defaultValue, List<Double> fmListValues) {
//		double result = fmListValues.stream().reduce(0.0, (currentSum, currentValue) -> (currentSum) + (currentValue-defaultValue));
//		
//		double average = result/fmListValues.size();
//		
//		
//		
//	
//		double value = Double.parseDouble(FM_FORMAT_CALCULATE.format(fmMeasurement.getValue()-fmMeasurement.getDefaultValue()));
//	}
//	
//	private double calcDiffenceBetweenMeasurementAndNominalValue(double measurementValue, double defaultValue) {
//		return Double.parseDouble(FM_FORMAT_CALCULATE.format());
//	}

}
