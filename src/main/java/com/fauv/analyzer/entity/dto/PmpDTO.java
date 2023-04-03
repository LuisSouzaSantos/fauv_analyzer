package com.fauv.analyzer.entity.dto;

import java.util.List;

import com.fauv.analyzer.enums.AxisType;

public class PmpDTO {

	private Long id;
	private String name;
	private AxisType axis;
	private double x;
	private double y;
	private double z;
	private boolean active;
	private List<NominalAxisCoordinateDTO> axisCoordinateList;
	
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
	
	public AxisType getAxis() {
		return axis;
	}
	public void setAxis(AxisType axis) {
		this.axis = axis;
	}
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getZ() {
		return z;
	}
	
	public void setZ(double z) {
		this.z = z;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public List<NominalAxisCoordinateDTO> getAxisCoordinateList() {
		return axisCoordinateList;
	}
	
	public void setAxisCoordinateList(List<NominalAxisCoordinateDTO> axisCoordinateList) {
		this.axisCoordinateList = axisCoordinateList;
	}
	
}
