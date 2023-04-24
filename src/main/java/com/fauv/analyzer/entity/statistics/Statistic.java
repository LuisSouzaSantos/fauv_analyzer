package com.fauv.analyzer.entity.statistics;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fauv.analyzer.entity.dto.FmDTO;
import com.fauv.analyzer.entity.dto.PmpDTO;

public class Statistic {

	private String carName;
	private String partNumber;
	private String unitName;
	private LocalDate initDate;
	private LocalDate endDate;
	private Long modelId;
	private long numberOfSamples;
	private long numberOfDefectiveSamples;
	private long totalAk;
	private long totalBk;
	private long totalIo;
	private List<Long> samplesIds = new ArrayList<>();
	private List<FmDTO> defaultFmNames = new ArrayList<FmDTO>();
	private List<PmpDTO> defaultPmpNames = new ArrayList<PmpDTO>();
	
	public String getCarName() {
		return carName;
	}
	
	public void setCarName(String carName) {
		this.carName = carName;
	}
	
	public String getPartNumber() {
		return partNumber;
	}
	
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	
	public String getUnitName() {
		return unitName;
	}
	
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	public LocalDate getInitDate() {
		return initDate;
	}
	
	public void setInitDate(LocalDate initDate) {
		if  (this.getInitDate() != null && this.getInitDate().isBefore(initDate)) { return; }
		
		this.initDate = initDate;
	}
	
	public LocalDate getEndDate() {
		return endDate;
	}
	
	public void setEndDate(LocalDate endDate) {
		if  (this.getEndDate() != null && this.getEndDate().isAfter(endDate)) { return; }
		
		this.endDate = endDate;
	}
	
	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	public long getNumberOfSamples() {
		return numberOfSamples;
	}
	
	public void setNumberOfSamples(long numberOfSamples) {
		this.numberOfSamples = numberOfSamples;
	}
	
	public long getNumberOfDefectiveSamples() {
		return numberOfDefectiveSamples;
	}
	
	public void setNumberOfDefectiveSamples(long numberOfDefectiveSamples) {
		this.numberOfDefectiveSamples = numberOfDefectiveSamples;
	}
	
	public long getTotalAk() {
		return totalAk;
	}
	
	public void setTotalAk(long totalAk) {
		this.totalAk = totalAk;
	}
	
	public long getTotalBk() {
		return totalBk;
	}
	
	public void setTotalBk(long totalBk) {
		this.totalBk = totalBk;
	}
	
	public long getTotalIo() {
		return totalIo;
	}
	
	public void setTotalIo(long totalIo) {
		this.totalIo = totalIo;
	}

	public List<Long> getSamplesIds() {
		return samplesIds;
	}

	public void setSamplesIds(List<Long> samplesIds) {
		this.samplesIds = samplesIds;
	}

	public List<FmDTO> getDefaultFmNames() {
		return defaultFmNames;
	}

	public void setDefaultFmNames(List<FmDTO> defaultFmNames) {
		this.defaultFmNames = defaultFmNames;
	}

	public List<PmpDTO> getDefaultPmpNames() {
		return defaultPmpNames;
	}

	public void setDefaultPmpNames(List<PmpDTO> defaultPmpNames) {
		this.defaultPmpNames = defaultPmpNames;
	}

}
