
package com.fauv.analyzer.entity.form;

import java.util.HashSet;
import java.util.Set;

import com.fauv.analyzer.entity.dto.CarDTO;

public class ModelForm {
	
	private String partNumber;
	private CarDTO car;
	private String stepDescription;
	private Set<PmpForm> pmpList = new HashSet<PmpForm>();
	private Set<FmForm> fmList = new HashSet<FmForm>();
	
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

	public Set<PmpForm> getPmpList() {
		return pmpList;
	}

	public void setPmpList(Set<PmpForm> pmpList) {
		this.pmpList = pmpList;
	}

	public Set<FmForm> getFmList() {
		return fmList;
	}

	public void setFmList(Set<FmForm> fmList) {
		this.fmList = fmList;
	}
		
}
