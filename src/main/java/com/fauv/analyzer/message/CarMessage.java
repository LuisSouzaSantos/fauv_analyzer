package com.fauv.analyzer.message;

public class CarMessage {

	public static final String FORM_ID = "ID_CANNOT_BE_NULL";
	public static final String FORM_NAME = "NAME_CANNOT_BE_NULL_OR_EMPTLY";
	public static final String FORM_NAME_SIZE = "NAME_MUST_HAVE_BETWEEN_1_AND_255_CHARACTERS";
	public static final String FORM_UNIT_ID = "UNIT_ID_CANNOT_BE_NULL";
	
	public static final String ERROR_DUPLICATE_BY_NAME = "ALREADY_EXISTS_AN_CAR_WITH_THE_SAME_NAME";
	public static final String ERROR_NOT_FOUND = "CAR_NOT_FOUND";
	public static final String ERROR_INACTIVE = "CAR_INACTIVE";
	
}
