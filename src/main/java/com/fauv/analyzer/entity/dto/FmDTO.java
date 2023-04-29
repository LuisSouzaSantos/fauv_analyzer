package com.fauv.analyzer.entity.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fauv.analyzer.enums.AxisType;
import com.fauv.analyzer.enums.CatalogType;
import com.fauv.analyzer.enums.FmLevel;

public class FmDTO {
	
	private Long id;
	private String name;
	private double higherTolerance;
	private double lowerTolerance;
	private double defaultValue;
	private FmLevel level; 
	private AxisType axis;
	private boolean active;
	private CatalogType catalogType;
	@JsonIgnoreProperties(value = {"axisCoordinateList"})
	private List<PmpDTO> pmpList = new ArrayList<>();
	private List<FmImpactDTO> fmImpactList = new ArrayList<>();
	
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
	
	public double getHigherTolerance() {
		return higherTolerance;
	}
	
	public void setHigherTolerance(double higherTolerance) {
		this.higherTolerance = higherTolerance;
	}
	
	public double getLowerTolerance() {
		return lowerTolerance;
	}
	
	public void setLowerTolerance(double lowerTolerance) {
		this.lowerTolerance = lowerTolerance;
	}
	
	public double getDefaultValue() {
		return defaultValue;
	}
	
	public void setDefaultValue(double defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public FmLevel getLevel() {
		return level;
	}

	public void setLevel(FmLevel level) {
		this.level = level;
	}

	public AxisType getAxis() {
		return axis;
	}
	
	public void setAxis(AxisType axis) {
		this.axis = axis;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public CatalogType getCatalogType() {
		return catalogType;
	}

	public void setCatalogType(CatalogType catalogType) {
		this.catalogType = catalogType;
	}

	public List<PmpDTO> getPmpList() {
		return pmpList;
	}

	public void setPmpList(List<PmpDTO> pmpList) {
		this.pmpList = pmpList;
	}

	public List<FmImpactDTO> getFmImpactList() {
		return fmImpactList;
	}

	public void setFmImpactList(List<FmImpactDTO> fmImpactList) {
		this.fmImpactList = fmImpactList;
	}
	
}
