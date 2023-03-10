package com.fauv.analyzer.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fauv.analyzer.entity.form.PmpForm;
import com.fauv.analyzer.enums.AxisType;

@Entity
@Table(name = "nominal_pmp", schema = "analyzer")
public class NominalPmp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	@Enumerated(EnumType.STRING)
	private AxisType axis;
	private BigDecimal x;
	private BigDecimal y;
	private BigDecimal z;
	private boolean active = true;
	
	@ManyToOne
    private Model model;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "nominal_pmp_id")
	private List<NominalAxisCoordinate> axisCoordinateList;
	
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
	
	public AxisType getAxis() {
		return axis;
	}

	public void setAxis(AxisType axis) {
		this.axis = axis;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public List<NominalAxisCoordinate> getAxisCoordinateList() {
		return axisCoordinateList;
	}
	
	public void setAxisCoordinateList(List<NominalAxisCoordinate> axisCoordinateList) {
		this.axisCoordinateList = axisCoordinateList;
	}

	public static NominalPmp buildNominalPmp(PmpForm pmpForm) {
		NominalPmp nominalPmp = new NominalPmp();
		nominalPmp.setActive(pmpForm.isActive());
		nominalPmp.setName(pmpForm.getName());
		nominalPmp.setAxis(pmpForm.getAxis());
		nominalPmp.setX(new BigDecimal(pmpForm.getX()));
		nominalPmp.setY(new BigDecimal(pmpForm.getY()));
		nominalPmp.setZ(new BigDecimal(pmpForm.getZ()));
		
		nominalPmp.setAxisCoordinateList(pmpForm.getAxisCoordinateList().stream()
				.map(axisCoordinate -> NominalAxisCoordinate.build(axisCoordinate, nominalPmp))
				.collect(Collectors.toList()));
		
		return nominalPmp;
	}
	
}
