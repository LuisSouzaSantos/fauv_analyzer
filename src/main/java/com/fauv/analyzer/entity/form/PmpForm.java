package com.fauv.analyzer.entity.form;

import java.util.List;
import java.util.Objects;

import com.fauv.analyzer.enums.AxisType;

public class PmpForm {
	
	private String name;
	private AxisType axis;
	private double x;
	private double y;
	private double z;
	private boolean active;
	private List<NominalAxisCoordinateForm> axisCoordinateList;
	
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
	
	public List<NominalAxisCoordinateForm> getAxisCoordinateList() {
		return axisCoordinateList;
	}
	
	public void setAxisCoordinateList(List<NominalAxisCoordinateForm> axisCoordinateList) {
		this.axisCoordinateList = axisCoordinateList;
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
		PmpForm other = (PmpForm) obj;
		return Objects.equals(name, other.name);
	}
	
}
