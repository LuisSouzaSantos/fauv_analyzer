package com.fauv.analyzer.utils;

import java.text.DecimalFormat;
import java.util.List;

public class Utils {
	
	private static final DecimalFormat DF_FORMAT = new DecimalFormat("#.###");
	private static final DecimalFormat NORMAL_DISTRIBUTION_FORMAT = new DecimalFormat("#.###");

	public static double formatDoubleNumberToCorretFormat(double number) {
		String formatted = DF_FORMAT.format(number);
		
		return Double.parseDouble(formatted);
	}
	
	public static double formatNomalDistribution(double number) {
		String formatted = NORMAL_DISTRIBUTION_FORMAT.format(number);
		
		return Double.parseDouble(formatted);
	}
	
	public static double sumDoubleValueList(List<Double> fmListValues) {
		return fmListValues.stream().reduce(0.0, (currentSum, currentValue) -> currentSum + currentValue);
	}
	
}
