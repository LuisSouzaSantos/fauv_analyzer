package com.fauv.analyzer.entity.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FmHelper {

	private String name;
	private List<CoordinateHelper> nominalCoordinates = new ArrayList<CoordinateHelper>();
	private NominalAxisCoordinateHelper nominalAxisCoodinates;
	private List<CoordinateHelper> measurementCoordinates = new ArrayList<CoordinateHelper>();
	private MeasurementAxisCoordinateHelper measurementAxisCoordinates;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<CoordinateHelper> getNominalCoordinates() {
		return nominalCoordinates;
	}
	
	public void setNominalCoordinates(List<CoordinateHelper> nominalCoordinates) {
		this.nominalCoordinates = nominalCoordinates;
	}
	
	public NominalAxisCoordinateHelper getNominalAxisCoodinates() {
		return nominalAxisCoodinates;
	}
	
	public void setNominalAxisCoodinates(NominalAxisCoordinateHelper nominalAxisCoodinates) {
		this.nominalAxisCoodinates = nominalAxisCoodinates;
	}
	
	public List<CoordinateHelper> getMeasurementCoordinates() {
		return measurementCoordinates;
	}
	
	public void setMeasurementCoordinates(List<CoordinateHelper> measurementCoordinates) {
		this.measurementCoordinates = measurementCoordinates;
	}
	
	public MeasurementAxisCoordinateHelper getMeasurementAxisCoordinates() {
		return measurementAxisCoordinates;
	}
	
	public void setMeasurementAxisCoordinates(MeasurementAxisCoordinateHelper measurementAxisCoordinates) {
		this.measurementAxisCoordinates = measurementAxisCoordinates;
	}
	
	public List<String> getPmpNameList() {
		return getNominalCoordinates().stream().map(nominalCoordinate -> nominalCoordinate.getName()).collect(Collectors.toList());
	}

}
