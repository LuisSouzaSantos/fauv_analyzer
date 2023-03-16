package com.fauv.analyzer.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fauv.analyzer.entity.dto.NominalAxisCoordinateDTO;
import com.fauv.analyzer.entity.form.NominalAxisCoordinateForm;
import com.fauv.analyzer.enums.AxisType;
import com.fauv.analyzer.message.ModelMessage;

@Entity
@Table(name = "nominal_axis_coordinate", schema = "analyzer")
public class NominalAxisCoordinate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = ModelMessage.NOMINAL_AXIS_COORDINATE_NAME)
	private String name;
	
	@NotNull(message = ModelMessage.NOMINAL_AXIS_COORDINATE_LOWER_TOLERANCE)
	private BigDecimal lowerTolerance;
	
	@NotNull(message = ModelMessage.NOMINAL_AXIS_COORDINATE_HIGHER_TOLERANCE)
	private BigDecimal higherTolerance;
	
	@NotNull(message = ModelMessage.NOMINAL_AXIS_COORDINATE_AXIS)
	@Enumerated(EnumType.STRING)
	private AxisType axis;
	
	@ManyToOne
    private NominalPmp nominalPmp;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public BigDecimal getLowerTolerance() {
		return lowerTolerance;
	}

	public void setLowerTolerance(BigDecimal lowerTolerance) {
		this.lowerTolerance = lowerTolerance;
	}

	public BigDecimal getHigherTolerance() {
		return higherTolerance;
	}

	public void setHigherTolerance(BigDecimal higherTolerance) {
		this.higherTolerance = higherTolerance;
	}

	public AxisType getAxis() {
		return axis;
	}
	
	public void setAxis(AxisType axis) {
		this.axis = axis;
	}
	
	public NominalPmp getNominalPmp() {
		return nominalPmp;
	}

	public void setNominalPmp(NominalPmp nominalPmp) {
		this.nominalPmp = nominalPmp;
	}

	public static NominalAxisCoordinate build(NominalAxisCoordinateForm form, NominalPmp nominalPmp) {
		NominalAxisCoordinate nominalAxisCoordinate = new NominalAxisCoordinate();
		
		nominalAxisCoordinate.setName(form.getName());
		nominalAxisCoordinate.setAxis(form.getAxis());
		nominalAxisCoordinate.setLowerTolerance(new BigDecimal(form.getLowerTolerance()));
		nominalAxisCoordinate.setHigherTolerance(new BigDecimal(form.getHigherTolerance()));
		nominalAxisCoordinate.setNominalPmp(nominalPmp);
		
		return nominalAxisCoordinate;
	}
	
	
	public static NominalAxisCoordinate build(NominalAxisCoordinateDTO nominalAxisCoordinateDTO, NominalPmp nominalPmp) {
		NominalAxisCoordinate nominalAxisCoordinate = new NominalAxisCoordinate();
		
		nominalAxisCoordinate.setName(nominalAxisCoordinateDTO.getName());
		nominalAxisCoordinate.setAxis(nominalAxisCoordinateDTO.getAxis());
		nominalAxisCoordinate.setLowerTolerance(new BigDecimal(nominalAxisCoordinateDTO.getLowerTolerance()));
		nominalAxisCoordinate.setHigherTolerance(new BigDecimal(nominalAxisCoordinateDTO.getHigherTolerance()));
		nominalAxisCoordinate.setNominalPmp(nominalPmp);
		
		return nominalAxisCoordinate;
	}

	public static NominalAxisCoordinate build(NominalAxisCoordinate axisCoordinate, NominalPmp nominalPmp) {
		NominalAxisCoordinate nominalAxisCoordinate = new NominalAxisCoordinate();
		
		nominalAxisCoordinate.setName(nominalPmp.getName());
		nominalAxisCoordinate.setAxis(nominalPmp.getAxis());
		nominalAxisCoordinate.setLowerTolerance(axisCoordinate.getLowerTolerance());
		nominalAxisCoordinate.setHigherTolerance(axisCoordinate.getHigherTolerance());
		nominalAxisCoordinate.setNominalPmp(nominalPmp);
		
		return nominalAxisCoordinate;
	}

}
