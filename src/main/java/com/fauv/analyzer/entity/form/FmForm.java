package com.fauv.analyzer.entity.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fauv.analyzer.entity.dto.PmpDTO;
import com.fauv.analyzer.enums.AxisType;
import com.fauv.analyzer.enums.FmLevel;

public class FmForm {
	
	private String name;
	private double higherTolerance;
	private double lowerTolerance;
	private double defaultValue;
	private AxisType axis;
	private FmLevel level = FmLevel.MEDIUM;
	private boolean active = true;
	
	private List<FmImpactForm> fmImpactList = new ArrayList<>();
	private List<PmpDTO> pmpList = new ArrayList<>();
	
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
	
	public List<FmImpactForm> getFmImpactList() {
		return fmImpactList;
	}

	public void setFmImpactList(List<FmImpactForm> fmImpactList) {
		this.fmImpactList = fmImpactList;
	}

	public List<PmpDTO> getPmpList() {
		return pmpList;
	}

	public void setPmpList(List<PmpDTO> pmpList) {
		this.pmpList = pmpList;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FmForm other = (FmForm) obj;
		return Objects.equals(name, other.name);
	}
	
}
