package com.fauv.analyzer.entity.dto;

import java.util.HashSet;
import java.util.Set;

public class MeasurementPmpDTO {
	
	private String name;
	private double defaultX;
	private double x;
	private double defaultY;
	private double y;
	private double defaultZ;
	private double z;
	private Set<MeasurementAxisCoordinateDTO> measurementAxisCoordinateList = new HashSet<>();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getDefaultX() {
		return defaultX;
	}
	
	public void setDefaultX(double defaultX) {
		this.defaultX = defaultX;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getDefaultY() {
		return defaultY;
	}
	
	public void setDefaultY(double defaultY) {
		this.defaultY = defaultY;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getDefaultZ() {
		return defaultZ;
	}
	
	public void setDefaultZ(double defaultZ) {
		this.defaultZ = defaultZ;
	}
	
	public double getZ() {
		return z;
	}
	
	public void setZ(double z) {
		this.z = z;
	}
	
	public Set<MeasurementAxisCoordinateDTO> getMeasurementAxisCoordinateList() {
		return measurementAxisCoordinateList;
	}
	
	public void setMeasurementAxisCoordinateList(Set<MeasurementAxisCoordinateDTO> measurementAxisCoordinateList) {
		this.measurementAxisCoordinateList = measurementAxisCoordinateList;
	}
		
}
