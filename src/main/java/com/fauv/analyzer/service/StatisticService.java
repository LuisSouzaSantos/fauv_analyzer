package com.fauv.analyzer.service;

import java.util.List;

import com.fauv.analyzer.entity.statistics.Statistic;

public interface StatisticService {
	
	public List<Statistic> getAll(Long unitId);
	
}