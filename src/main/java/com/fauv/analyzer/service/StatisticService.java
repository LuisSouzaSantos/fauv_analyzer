package com.fauv.analyzer.service;

import java.util.List;
import com.fauv.analyzer.entity.dto.StatisticDTO;
import com.fauv.analyzer.exception.SampleException;

public interface StatisticService {

	public StatisticDTO getByIdValidateIt(Long id) throws SampleException;
	
	public List<StatisticDTO> getAll();
	
}
