package com.fauv.analyzer.service;

import java.util.List;

import com.fauv.analyzer.entity.Model;
import com.fauv.analyzer.entity.statistics.FmStatistic;
import com.fauv.analyzer.entity.statistics.Statistic;

public interface StatisticService {
	
	public List<Statistic> getAll(Long unitId);
	
	public FmStatistic generateFmStatistic(Long modelId, String fmName);
	
}