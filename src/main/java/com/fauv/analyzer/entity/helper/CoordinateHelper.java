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
	
	public void addNewCoordinate(CoordinateValueHelper coordinateValue) {
		coordinateValue.setAxisType(defineNewAxisTypeAccordingIndex());
		this.getValues().add(coordinateValue);
		
	}
	
	private AxisType defineNewAxisTypeAccordingIndex() {
		if (values.size() == 0) { return AxisType.X; }
		if (values.size() == 1) { return AxisType.Y; }
		if (values.size() == 2) { return AxisType.Z; }
		if (values.size() == 3) { return AxisType.D; }
		if (values.size() == 4) { return AxisType.T; }
		
		return null;
	}
	
}
