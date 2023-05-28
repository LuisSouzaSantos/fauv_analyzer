package com.fauv.analyzer.entity.statistics;

import java.util.ArrayList;
import java.util.List;

import com.fauv.analyzer.entity.dto.FmDTO;
import com.fauv.analyzer.enums.AxisType;

public class PmpStatisticsContainer {

	private String name;
	private List<FmDTO> mappedFmList = new ArrayList<>();
	private List<AxisType> axisList = new ArrayList<>();
	private List<PmpStatistics> pmpStatisticsList = new ArrayList<>();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<FmDTO> getMappedFmList() {
		return mappedFmList;
	}

	public void setMappedFmList(List<FmDTO> mappedFmList) {
		this.mappedFmList = mappedFmList;
	}

	public List<AxisType> getAxisList() {
		return axisList;
	}

	public void setAxisList(List<AxisType> axisList) {
		this.axisList = axisList;
	}

	public List<PmpStatistics> getPmpStatisticsList() {
		return pmpStatisticsList;
	}
	
	public void setPmpStatisticsList(List<PmpStatistics> pmpStatisticsList) {
		this.pmpStatisticsList = pmpStatisticsList;
	}
	
}
