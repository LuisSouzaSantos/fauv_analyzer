package com.fauv.analyzer.entity.dto;

import java.util.List;

public class ModelDTO {
	
	private Long id;
	private String partNumber;
	private CarDTO car;
	private String stepDescription;
	private List<PmpDTO> pmpList;
	private List<FmDTO> fmList;
	
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

	public CarDTO getCar() {
		return car;
	}

	public void setCar(CarDTO car) {
		this.car = car;
	}

	public String getStepDescription() {
		return stepDescription;
	}

	public void setStepDescription(String stepDescription) {
		this.stepDescription = stepDescription;
	}

	public List<PmpDTO> getPmpList() {
		return pmpList;
	}

	public void setPmpList(List<PmpDTO> pmpList) {
		this.pmpList = pmpList;
	}

	public List<FmDTO> getFmList() {
		return fmList;
	}

	public void setFmList(List<FmDTO> fmList) {
		this.fmList = fmList;
	}
		
}
