package com.fauv.analyzer.entity.dto;

import java.time.LocalDateTime;

public class SampleStatisticsLoadingDTO {

	private Long id;
	private String partNumber;
	private String unitName;
	private String carName;
	private Long modelId;
	private LocalDateTime initDate;
	private LocalDateTime endDate;
	private int ak;
	private int bk;
	private int io;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
	
	public String getCarName() {
		return carName;
	}
	
	public void setCarName(String carName) {
		this.carName = carName;
	}
	
	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	public LocalDateTime getInitDate() {
		return initDate;
	}
	
	public void setInitDate(LocalDateTime initDate) {
		this.initDate = initDate;
	}
	
	public LocalDateTime getEndDate() {
		return endDate;
	}
	
	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}
	
	public int getAk() {
		return ak;
	}
	
	public void setAk(int ak) {
		this.ak = ak;
	}
	
	public int getBk() {
		return bk;
	}
	
	public void setBk(int bk) {
		this.bk = bk;
	}
	
	public int getIo() {
		return io;
	}
	
	public void setIo(int io) {
		this.io = io;
	}
	
}