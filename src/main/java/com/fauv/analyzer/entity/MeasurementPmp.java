package com.fauv.analyzer.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fauv.analyzer.message.SampleMessage;

@Entity
@Table(name = "measurement_pmp", schema = "analyzer")
public class MeasurementPmp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = SampleMessage.MEASUREMENT_PMP_X)
	private BigDecimal x;
	
	@NotNull(message = SampleMessage.MEASUREMENT_PMP_Y)
	private BigDecimal y;
	
	@NotNull(message = SampleMessage.MEASUREMENT_PMP_Z)
	private BigDecimal z;
	
	@NotNull(message = SampleMessage.MEASUREMENT_PMP_ASSOCIATION)
	@ManyToOne
	@JoinColumn(name = "nominal_pmp_id")
	private NominalPmp nominalPmp;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "measurement_pmp_id")
	private Set<MeasurementAxisCoordinate> measurementAxisCoordinateList = new HashSet<>();
	
	@ManyToOne
	private Sample sample;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getX() {
		return x;
	}

	public void setX(BigDecimal x) {
		this.x = x;
	}

	public BigDecimal getY() {
		return y;
	}

	public void setY(BigDecimal y) {
		this.y = y;
	}

	public BigDecimal getZ() {
		return z;
	}

	public void setZ(BigDecimal z) {
		this.z = z;
	}

	public NominalPmp getNominalPmp() {
		return nominalPmp;
	}

	public void setNominalPmp(NominalPmp nominalPmp) {
		this.nominalPmp = nominalPmp;
	}

	public Set<MeasurementAxisCoordinate> getMeasurementAxisCoordinateList() {
		return measurementAxisCoordinateList;
	}

	public void setMeasurementAxisCoordinateList(Set<MeasurementAxisCoordinate> measurementAxisCoordinateList) {
		this.measurementAxisCoordinateList = measurementAxisCoordinateList;
	}

	public Sample getSample() {
		return sample;
	}

	public void setSample(Sample sample) {
		this.sample = sample;
	}
	
}
