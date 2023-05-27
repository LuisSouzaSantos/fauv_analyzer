package com.fauv.analyzer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fauv.analyzer.entity.statistics.FmStatistic;
import com.fauv.analyzer.entity.statistics.PmpStatisticsContainer;
import com.fauv.analyzer.entity.statistics.Statistic;
import com.fauv.analyzer.exception.ModelException;
import com.fauv.analyzer.exception.SampleException;
import com.fauv.analyzer.exception.StatisticException;
import com.fauv.analyzer.service.StatisticService;

@CrossOrigin
@RestController
@RequestMapping("/statistic")
public class StatisticController {

	@Autowired
	private StatisticService statisticService;
	
	@GetMapping("/{unitId}")
	public ResponseEntity<List<Statistic>> getAllStatisticsByUnit(@PathVariable Long unitId) throws SampleException {		
		return ResponseEntity.ok(statisticService.getAllStatisticsByUnit(unitId)); 
	}
	
	@GetMapping("/fm/{modelId}/{fmName}")
	public ResponseEntity<FmStatistic> generateFmStatistic(@PathVariable Long modelId, @PathVariable String fmName) throws ModelException, StatisticException {
		FmStatistic fmStatistic = statisticService.generateFmStatistic(modelId, fmName);
		
		return ResponseEntity.ok(fmStatistic); 
	}
	
	@GetMapping("/pmp/{modelId}/{pmpName}")
	public ResponseEntity<PmpStatisticsContainer> generatePmpStatistic(@PathVariable Long modelId, @PathVariable String pmpName) throws ModelException, StatisticException {
		return ResponseEntity.ok(statisticService.generatePmpStatistic(modelId, pmpName)); 
	}

}
