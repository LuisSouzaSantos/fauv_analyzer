package com.fauv.analyzer.entity.statistics;

import java.util.ArrayList;
import java.util.List;

public class FmGraphic {

	private double higherTolerance;
	private double lowerTolerance;
	private double nominalTolerance;
	private double zoneDivision;
	private List<DetailedFmGraphic> detailedFmGraphicsList = new ArrayList<>();
	
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
	
	public double getNominalTolerance() {
		return nominalTolerance;
	}
	
	public void setNominalTolerance(double nominalTolerance) {
		this.nominalTolerance = nominalTolerance;
	}
	
	public double getZoneDivision() {
		return zoneDivision;
	}
	
	public void setZoneDivision(double zoneDivision) {
		this.zoneDivision = zoneDivision;
	}
	
	public List<DetailedFmGraphic> getDetailedFmGraphicsList() {
		return detailedFmGraphicsList;
	}
	
	public void setDetailedFmGraphicsList(List<DetailedFmGraphic> detailedFmGraphicsList) {
		this.detailedFmGraphicsList = detailedFmGraphicsList;
	}
	
}
