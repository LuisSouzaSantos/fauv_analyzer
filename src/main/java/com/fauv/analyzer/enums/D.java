package com.fauv.analyzer.enums;

import java.util.Arrays;
import java.util.List;

public enum D {
	
	D2(1.128, 1.693, 2.059, 2.326, 2.534, 2.704, 2.847, 2.970, 3.078, 3.173,3.258,3.336,3.407,3.472,3.532,3.588,3.640,3.689,3.735,3.778,3.819,3.858, 3.895, 3.931),
	D3(0.0, 0.0, 0.0, 0.0, 0.0, 0.076,0.136,0.184,0.223,0.256,0.283,0.307,0.328,0.347,0.363,0.378,0.391,0.403,0.415,0.425,0.434,0.443,0.451,0.459),
	D4(3.267, 2.574, 2.282, 2.114, 2.004, 1.924, 1.864, 1.816, 1.777, 1.744, 1.717, 1.693, 1.672, 1.653, 1.637, 1.622, 1.608, 1.597, 1.585,1.575,1.566,1.557,1.548,1.541);
	
	private List<Double> constant;
	
	private D(Double... constant) {
		this.constant = Arrays.asList(constant);
	}
	
	public Double getConstant(int numberOfSamples) {
		if (numberOfSamples <=1) { numberOfSamples = 2; }
		if (numberOfSamples >=26 ) { numberOfSamples = 25; }
		
		int index = numberOfSamples-2;
		
		return constant.get(index);
	}
	
	
}
