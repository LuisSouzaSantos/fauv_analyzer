package com.fauv.analyzer.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fauv.analyzer.message.ModelMessage;

@Entity
@Table(name = "model", schema = "analyzer")
public class Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = ModelMessage.FORM_PART_NUMBER)
	@Size(min = 1, max = 255, message = ModelMessage.FORM_PART_NUMBER_SIZE)
	private String partNumber;
	
	@NotNull(message = ModelMessage.FORM_CAR)
	@ManyToOne
    @JoinColumn(name = "car_id")
	private Car car;
	
	@NotBlank(message = ModelMessage.FORM_STEP_DESCRIPTION)
	@Size(min = 1, max = 255, message = ModelMessage.FORM_STEP_DESCRIPTION_SIZE)
	private String stepDescription;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "model_id")
	@OrderBy("name")
	private List<NominalPmp> pmpList = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "model_id")
	@OrderBy("name")
	private List<NominalFm> fmList = new ArrayList<>();
	
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
	
	public Car getCar() {
		return car;
	}
	
	public void setCar(Car car) {
		this.car = car;
	}
	
	public String getStepDescription() {
		return stepDescription;
	}
	
	public void setStepDescription(String stepDescription) {
		this.stepDescription = stepDescription;
	}
	
	public List<NominalPmp> getPmpList() {
		return pmpList;
	}
	
	public void setPmpList(List<NominalPmp> pmpList) {
		this.pmpList = pmpList;
	}
	
	public List<NominalFm> getFmList() {
		return fmList;
	}
	
	public void setFmList(List<NominalFm> fmList) {
		this.fmList = fmList;
	}
	
	public NominalPmp getPmpByName(String name) {
		return this.getPmpList().stream().filter(pmp -> pmp.getName().equals(name)).findFirst().orElse(null);
	}
	
	public NominalPmp getPmpById(Long id) {
		return this.getPmpList().stream().filter(pmp -> pmp.getId().equals(id)).findFirst().orElse(null);
	}
	
	public NominalFm getFmByName(String name) {
		return this.getFmList().stream().filter(fm -> fm.getName().equals(name)).findFirst().orElse(null);
	}
	
	public NominalFm getFmById(Long id) {
		return this.getFmList().stream().filter(fm -> fm.getId().equals(id)).findFirst().orElse(null);
	}

	@Override
	public int hashCode() {
		return Objects.hash(car, partNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Model other = (Model) obj;
		return Objects.equals(car, other.car) && Objects.equals(partNumber, other.partNumber);
	}
	
}
