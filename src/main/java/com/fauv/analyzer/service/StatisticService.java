package com.fauv.analyzer.service;

import java.util.List;

import com.fauv.analyzer.entity.statistics.FmStatistic;
import com.fauv.analyzer.entity.statistics.PmpStatisticsContainer;
import com.fauv.analyzer.entity.statistics.Statistic;
import com.fauv.analyzer.exception.ModelException;
import com.fauv.analyzer.exception.StatisticException;

public interface StatisticService {
	
	public List<Statistic> getAllStatisticsByUnit(Long unitId);
	
	public FmStatistic generateFmStatistic(Long modelId, String fmName) throws ModelException, StatisticException;
	
	public PmpStatisticsContainer generatePmpStatistic(Long modelId, String pmpName) throws ModelException, StatisticException;
	
}