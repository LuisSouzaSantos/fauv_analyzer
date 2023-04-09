package com.fauv.analyzer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fauv.analyzer.entity.statistics.Statistic;
import com.fauv.analyzer.exception.SampleException;
import com.fauv.analyzer.service.StatisticService;

@CrossOrigin
@RestController
@RequestMapping("/statistic")
public class StatisticController {

	@Autowired
	private StatisticService statisticService;
	
	@GetMapping("/{unitId}")
	public ResponseEntity<List<Statistic>> getByUnitId(@PathVariable Long unitId) throws SampleException {		
		return ResponseEntity.ok(statisticService.getAll(unitId)); 
	}
	
}
