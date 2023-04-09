package com.fauv.analyzer.entity.helper;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fauv.analyzer.message.DMOMessage;

//This entity define a Car PMP
public class PmpHelper {

	@NotBlank(message = DMOMessage.PMP_NAME)
	private String name;
	private CoordinateHelper nominalCoordinate;
	private List<NominalAxisCoordinateHelper> nominalAxisCoordinates = new ArrayList<>();
	private CoordinateHelper measurementCoordinate;
	private List<MeasurementAxisCoordinateHelper> measurementAxisCoordinates = new ArrayList<>();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public CoordinateHelper getNominalCoordinate() {
		return nominalCoordinate;
	}
	
	public void setNominalCoordinate(CoordinateHelper nominalCoordinate) {
		this.nominalCoordinate = nominalCoordinate;
	}
	
	public List<NominalAxisCoordinateHelper> getNominalAxisCoordinates() {
		return nominalAxisCoordinates;
	}
	
	public void setNominalAxisCoordinates(List<NominalAxisCoordinateHelper> nominalAxisCoordinates) {
		this.nominalAxisCoordinates = nominalAxisCoordinates;
	}
	
	public CoordinateHelper getMeasurementCoordinate() {
		return measurementCoordinate;
	}
	
	public void setMeasurementCoordinate(CoordinateHelper measurementCoordinate) {
		this.measurementCoordinate = measurementCoordinate;
	}
	
	public List<MeasurementAxisCoordinateHelper> getMeasurementAxisCoordinates() {
		return measurementAxisCoordinates;
	}
	
	public void setMeasurementAxisCoordinates(List<MeasurementAxisCoordinateHelper> measurementAxisCoordinates) {
		this.measurementAxisCoordinates = measurementAxisCoordinates;
	}

}
