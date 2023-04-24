package com.fauv.analyzer.entity.statistics;

import java.util.ArrayList;
import java.util.List;

import com.fauv.analyzer.enums.GraphicType;

public abstract class Graphic {

	protected GraphicType graphicType;
	protected double higherTolerance;
	protected double lowerTolerance;
	protected double midline;
	protected List<DetailedFmGraphic> detailedFmGraphicsList = new ArrayList<>();
	
	public GraphicType getGraphicType() {
		return graphicType;
	}
	
	public void setGraphicType(GraphicType graphicType) {
		this.graphicType = graphicType;
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
	
	public double getMidline() {
		return midline;
	}

	public void setMidline(double midline) {
		this.midline = midline;
	}

	public List<DetailedFmGraphic> getDetailedFmGraphicsList() {
		return detailedFmGraphicsList;
	}
	
	public void setDetailedFmGraphicsList(List<DetailedFmGraphic> detailedFmGraphicsList) {
		this.detailedFmGraphicsList = detailedFmGraphicsList;
	}
	
}
