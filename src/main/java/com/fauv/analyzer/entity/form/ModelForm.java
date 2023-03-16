
package com.fauv.analyzer.entity.form;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fauv.analyzer.entity.dto.CarDTO;
import com.fauv.analyzer.message.ModelMessage;

public class ModelForm {
	
	@NotBlank(message = ModelMessage.FORM_PART_NUMBER)
	@Size(min = 1, max = 255, message = ModelMessage.FORM_PART_NUMBER_SIZE)
	private String partNumber;
	
	@NotNull(message = ModelMessage.FORM_CAR)
	private CarDTO car;
	
	@NotBlank(message = ModelMessage.FORM_STEP_DESCRIPTION)
	@Size(min = 1, max = 255, message = ModelMessage.FORM_STEP_DESCRIPTION_SIZE)
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
