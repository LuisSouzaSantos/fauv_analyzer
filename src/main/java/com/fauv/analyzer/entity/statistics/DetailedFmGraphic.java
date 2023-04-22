package com.fauv.analyzer.entity.statistics;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fauv.analyzer.enums.StatisticCriteria;

public class DetailedFmGraphic {

	private Long sampleId;
	private String pin;
	private LocalDateTime updatedDate;
	private double value;
	private List<StatisticCriteria> statisticCriteriaList = new ArrayList<StatisticCriteria>();
	
	public DetailedFmGraphic(Long sampleId, String pin, LocalDateTime updatedDate, double value) {
		this.sampleId = sampleId;
		this.pin = pin;
		this.updatedDate = updatedDate;
		this.value = value;
	}

	public Long getSampleId() {
		return sampleId;
	}
	
	public void setSampleId(Long sampleId) {
		this.sampleId = sampleId;
	}
	
	public String getPin() {
		return pin;
	}
	
	public void setPin(String pin) {
		this.pin = pin;
	}
	
	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}
	
	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public List<StatisticCriteria> getStatisticCriteriaList() {
		return statisticCriteriaList;
	}
	
	public void setStatisticCriteriaList(List<StatisticCriteria> statisticCriteriaList) {
		this.statisticCriteriaList = statisticCriteriaList;
	}
	
}
