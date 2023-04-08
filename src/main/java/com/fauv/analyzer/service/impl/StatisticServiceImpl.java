package com.fauv.analyzer.service.impl;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fauv.analyzer.entity.Sample;
import com.fauv.analyzer.entity.dto.StatisticDTO;
import com.fauv.analyzer.exception.SampleException;
import com.fauv.analyzer.service.SampleService;
import com.fauv.analyzer.service.StatisticService;

@Service
public class StatisticServiceImpl implements StatisticService {
	
    private static final DecimalFormat FM_FORMAT_CALCULATE = new DecimalFormat("#.#");
	
	@Autowired
	private SampleService sampleService;
	
	@Override
	public StatisticDTO getByIdValidateIt(Long id) throws SampleException {
		Sample sample = sampleService.getByIdValidateIt(id);
		
		StatisticDTO statisticDTO = new StatisticDTO();
		statisticDTO.setCarName(sample.getModel().getCar().getName());
		statisticDTO.setPartNumber(sample.getModel().getPartNumber());
		statisticDTO.setUnitName(sample.getEquipment().getUnit().getName());
		
		return null;
	}

	@Override
	public List<StatisticDTO> getAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private boolean calculateLsc(double defaultValue, List<Double> fmListValues) {
		double result = fmListValues.stream().reduce(0.0, (currentSum, currentValue) -> (currentSum) + (currentValue-defaultValue));
		
		double average = result/fmListValues.size();
		
		
		
	
		double value = Double.parseDouble(FM_FORMAT_CALCULATE.format(fmMeasurement.getValue()-fmMeasurement.getDefaultValue()));
	}
	
	private double calcDiffenceBetweenMeasurementAndNominalValue(double measurementValue, double defaultValue) {
		return Double.parseDouble(FM_FORMAT_CALCULATE.format());
	}

}
