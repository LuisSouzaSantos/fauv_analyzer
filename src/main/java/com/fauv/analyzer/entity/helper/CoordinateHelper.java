package com.fauv.analyzer.entity.helper;

import java.util.ArrayList;
import java.util.List;

import com.fauv.analyzer.enums.AxisType;

public class CoordinateHelper {

	private String name;
	
	// The sequence is X, Y, Z, D and T
	private List<CoordinateValueHelper> values = new ArrayList<CoordinateValueHelper>();
	private AxisType workingOnAxis;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<CoordinateValueHelper> getValues() {
		return values;
	}

	public void setValues(List<CoordinateValueHelper> values) {
		this.values = values;
	}

	public AxisType getWorkingOnAxis() {
		return workingOnAxis;
	}
	
	public void setWorkingOnAxis(AxisType workingOnAxis) {
		this.workingOnAxis = workingOnAxis;
	}
	
}
