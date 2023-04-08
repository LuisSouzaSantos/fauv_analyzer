package com.fauv.analyzer.entity.dto;

import java.util.HashSet;
import java.util.Set;

import com.fauv.analyzer.enums.AxisType;
import com.fauv.analyzer.enums.CatalogType;
import com.fauv.analyzer.enums.FmLevel;
import com.fauv.analyzer.enums.ToleranceType;

public class MeasurementFmDTO {

	private String name;	
	private double higherTolerance;	
	private double lowerTolerance;	
	private double defaultValue;
	private CatalogType catalogType = CatalogType.DICHTIGKEIT;
	private AxisType axis;
	private FmLevel level;
	private double value;
	private ToleranceType toleranceType = ToleranceType.OUTOL;
	private Set<String> measurementPmpList = new HashSet<>();
	
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

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public ToleranceType getToleranceType() {
		return toleranceType;
	}
	
	public void setToleranceType(ToleranceType toleranceType) {
		this.toleranceType = toleranceType;
	}
	
	public Set<String> getMeasurementPmpList() {
		return measurementPmpList;
	}
	
	public void setMeasurementPmpList(Set<String> measurementPmpList) {
		this.measurementPmpList = measurementPmpList;
	}
	
}
