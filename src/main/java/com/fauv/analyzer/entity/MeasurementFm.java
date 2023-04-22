package com.fauv.analyzer.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fauv.analyzer.enums.ToleranceType;
import com.fauv.analyzer.message.SampleMessage;

@Entity
@Table(name = "measurement_fm", schema = "analyzer")
public class MeasurementFm {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = SampleMessage.MEASUREMENT_FM_ASSOCIATION)
	@ManyToOne
	private NominalFm nominalFm;
	
	@NotNull(message = SampleMessage.MEASUREMENT_FM_VALUE)
	private BigDecimal value;
	
	@NotNull(message = SampleMessage.MEASUREMENT_FM_TOLERANCE)
	@Enumerated(EnumType.STRING)
	private ToleranceType toleranceType = ToleranceType.OUTOL;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
	  name = "measurement_pmp_fm", 
	  joinColumns = @JoinColumn(name = "measurement_fm_id"), 
	  inverseJoinColumns = @JoinColumn(name = "measurement_pmp_id"))
	private Set<MeasurementPmp> measurementPmpList = new HashSet<>();
	
	@ManyToOne
	private Sample sample;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public NominalFm getNominalFm() {
		return nominalFm;
	}

	public void setNominalFm(NominalFm nominalFm) {
		this.nominalFm = nominalFm;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public ToleranceType getToleranceType() {
		return toleranceType;
	}

	public void setToleranceType(ToleranceType toleranceType) {
		this.toleranceType = toleranceType;
	}

	public Set<MeasurementPmp> getMeasurementPmpList() {
		return measurementPmpList;
	}

	public void setMeasurementPmpList(Set<MeasurementPmp> measurementPmpList) {
		this.measurementPmpList = measurementPmpList;
	}

	public Set<String> getMeasurementPmpNameList() {
		return getMeasurementPmpList().stream().map(measurementPmp -> measurementPmp.getNominalPmp().getName()).collect(Collectors.toSet());
	}

	public Sample getSample() {
		return sample;
	}

	public void setSample(Sample sample) {
		this.sample = sample;
	}
	
	public double getDefaultValue() {
		return getNominalFm().getDefaultValue().doubleValue();
	}
	
}
