package com.fauv.analyzer.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fauv.analyzer.enums.ToleranceType;
import com.fauv.analyzer.message.SampleMessage;

@Entity
@Table(name = "measurement_axis_coordinate", schema = "analyzer")
public class MeasurementAxisCoordinate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "nominal_axis_coordinate_id")
	private NominalAxisCoordinate nominalAxisCoordinate;
	
	@ManyToOne
	private MeasurementPmp measurementPmp;
	
	@NotNull(message = SampleMessage.MEASUREMENT_AXIS_COORDINATE_VALUE)
	private BigDecimal value;
	
	@NotNull(message = SampleMessage.MEASUREMENT_AXIS_COORDINATE_TOLARANCE)
	@Enumerated(EnumType.STRING)
	private ToleranceType toleranceType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public NominalAxisCoordinate getNominalAxisCoordinate() {
		return nominalAxisCoordinate;
	}

	public void setNominalAxisCoordinate(NominalAxisCoordinate nominalAxisCoordinate) {
		this.nominalAxisCoordinate = nominalAxisCoordinate;
	}

	public MeasurementPmp getMeasurementPmp() {
		return measurementPmp;
	}

	public void setMeasurementPmp(MeasurementPmp measurementPmp) {
		this.measurementPmp = measurementPmp;
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

}
