package com.fauv.analyzer.utils;

import java.text.DecimalFormat;
import java.util.List;

public class Utils {
	
	private static final DecimalFormat DF_FORMAT = new DecimalFormat("#.###");
	private static final DecimalFormat NORMAL_DISTRIBUTION_FORMAT = new DecimalFormat("#.##");
    private static final DecimalFormat FM_FORMAT_CALCULATE = new DecimalFormat("#.#####");
    private static final DecimalFormat PMP_FORMAT_CALCULATE = new DecimalFormat("#.##");
    private static final DecimalFormat FM_FORMAT_OVERVIEW = new DecimalFormat("#.#");
    private static final DecimalFormat PMP_FORMAT_OVERVIEW = new DecimalFormat("#.##");

	public static double formatDoubleNumberToCorretFormat(double number) {
		String formatted = DF_FORMAT.format(number);
		
		return Double.parseDouble(formatted);
	}
	
	public static double formatNomalDistribution(double number) {
		String formatted = NORMAL_DISTRIBUTION_FORMAT.format(number);
		
		return Double.parseDouble(formatted);
	}
	
	public static double formatNumberToFmGraphic(double number) {
		String formatted = FM_FORMAT_CALCULATE.format(number);
		
		return Double.parseDouble(formatted);
	}
	
	public static double formatNumberToPmpGraphic(double number) {
		String formatted = PMP_FORMAT_CALCULATE.format(number);
		
		return Double.parseDouble(formatted);
	}
	
	public static double formatNumberToPmpOverview(double number) {
		String formatted = PMP_FORMAT_OVERVIEW.format(number);
		
		return Double.parseDouble(formatted);
	}
	
	public static double formatNumberToFmOverview(double number) {
		String formatted = FM_FORMAT_OVERVIEW.format(number);
		
		return Double.parseDouble(formatted);
	}
	
	public static double sumDoubleValueList(List<Double> fmListValues) {
		return fmListValues.stream().reduce(0.0, (currentSum, currentValue) -> currentSum + currentValue);
	}
	
}
