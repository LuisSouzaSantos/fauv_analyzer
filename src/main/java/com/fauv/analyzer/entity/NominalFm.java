package com.fauv.analyzer.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fauv.analyzer.entity.form.FmForm;
import com.fauv.analyzer.enums.AxisType;
import com.fauv.analyzer.enums.CatalogType;
import com.fauv.analyzer.enums.FmLevel;

@Entity
@Table(name = "nominal_fm", schema = "analyzer")
public class NominalFm {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private BigDecimal higherTolerance;
	private BigDecimal lowerTolerance;
	private BigDecimal defaultValue;
	@Enumerated(EnumType.STRING)
	private CatalogType catalogType = CatalogType.DICHTIGKEIT;
	@Enumerated(EnumType.STRING)
	private AxisType axis = AxisType.X;
	@Enumerated(EnumType.STRING)
	private FmLevel level = FmLevel.MEDIUM;
	private boolean active = true;
	
	@ManyToOne
    private Model model;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "nominal_fm_id")
	private List<FmImpact> fmImpactList = new ArrayList<>();
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
	  name = "nominal_pmp_fm", 
	  joinColumns = @JoinColumn(name = "nominal_fm_id"), 
	  inverseJoinColumns = @JoinColumn(name = "nominal_pmp_id"))
	private List<NominalPmp> pointsUsingToMap = new ArrayList<>();
	
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
	
	public BigDecimal getHigherTolerance() {
		return higherTolerance;
	}

	public void setHigherTolerance(BigDecimal higherTolerance) {
		this.higherTolerance = higherTolerance;
	}

	public BigDecimal getLowerTolerance() {
		return lowerTolerance;
	}

	public void setLowerTolerance(BigDecimal lowerTolerance) {
		this.lowerTolerance = lowerTolerance;
	}

	public BigDecimal getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(BigDecimal defaultValue) {
		this.defaultValue = defaultValue;
	}

	public CatalogType getCatalogType() {
		return catalogType;
	}

	public void setCatalogType(CatalogType catalogType) {
		this.catalogType = catalogType;
	}

	public AxisType getAxis() {
		return axis;
	}

	public void setAxis(AxisType axis) {
		this.axis = axis;
	}

	public FmLevel getLevel() {
		return level;
	}

	public void setLevel(FmLevel level) {
		this.level = level;
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

	public List<FmImpact> getFmImpactList() {
		return fmImpactList;
	}

	public void setFmImpactList(List<FmImpact> fmImpactList) {
		this.fmImpactList = fmImpactList;
	}

	public List<NominalPmp> getPointsUsingToMap() {
		return pointsUsingToMap;
	}

	public void setPointsUsingToMap(List<NominalPmp> pointsUsingToMap) {
		this.pointsUsingToMap = pointsUsingToMap;
	}

	public static NominalFm buildNominalFm(FmForm fmForm) {
		NominalFm nominalFm = new NominalFm();
		nominalFm.setName(fmForm.getName());
		nominalFm.setActive(fmForm.isActive());
		nominalFm.setAxis(fmForm.getAxis());
		nominalFm.setDefaultValue(new BigDecimal(fmForm.getDefaultValue()));
		nominalFm.setLowerTolerance(new BigDecimal(fmForm.getLowerTolerance()));
		nominalFm.setHigherTolerance(new BigDecimal(fmForm.getHigherTolerance()));
		nominalFm.setLevel(fmForm.getLevel());
			
		return nominalFm;
	}

}
