package com.fauv.analyzer.entity.dto;

import java.util.ArrayList;
import java.util.List;

import com.fauv.analyzer.enums.AxisType;
import com.fauv.analyzer.enums.CatalogType;
import com.fauv.analyzer.enums.FmLevel;
import com.fauv.analyzer.enums.ToleranceType;
import com.fauv.analyzer.enums.ToleranceTypeStatus;

public class FmOverview {

	private String name;
	private AxisType axis;
	private CatalogType catalog;
	private ToleranceType tolerance;
	private List<String> pmpList = new ArrayList<>();
	private ToleranceTypeStatus toleranceStatus;
	private double higherTolerance;
	private double lowerTolerance;
	private double value;
	private FmLevel fmLevel;
	private boolean wasFound;
	
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
	public CatalogType getCatalog() {
		return catalog;
	}
	public void setCatalog(CatalogType catalog) {
		this.catalog = catalog;
	}
	public ToleranceType getTolerance() {
		return tolerance;
	}
	public void setTolerance(ToleranceType tolerance) {
		this.tolerance = tolerance;
	}
	public List<String> getPmpList() {
		return pmpList;
	}
	public void setPmpList(List<String> pmpList) {
		this.pmpList = pmpList;
	}
	public ToleranceTypeStatus getToleranceStatus() {
		return toleranceStatus;
	}
	public void setToleranceStatus(ToleranceTypeStatus toleranceStatus) {
		this.toleranceStatus = toleranceStatus;
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
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public FmLevel getFmLevel() {
		return fmLevel;
	}
	public void setFmLevel(FmLevel fmLevel) {
		this.fmLevel = fmLevel;
	}
	public boolean isWasFound() {
		return wasFound;
	}
	public void setWasFound(boolean wasFound) {
		this.wasFound = wasFound;
	}
	
}
