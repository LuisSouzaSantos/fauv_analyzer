package com.fauv.analyzer.utils;

import java.text.DecimalFormat;

public class Utils {
	
	private static final DecimalFormat DF = new DecimalFormat("#.###");

	public static double formatDoubleNumberToCorretFormat(double number) {
		String formatted = DF.format(number);
		
		return Double.parseDouble(formatted);
	}

}
