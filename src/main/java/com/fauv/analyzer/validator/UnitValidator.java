package com.fauv.analyzer.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fauv.analyzer.entity.Unit;
import com.fauv.analyzer.entity.form.UnitForm;
import com.fauv.analyzer.exception.UnitException;


@Service
public class UnitValidator {

	@Autowired
	private Validator validator;
	
	public void validatEquipmentFormFields(UnitForm form) throws UnitException {
		Set<ConstraintViolation<UnitForm>> violations = validator.validate(form);
		
		if (violations.isEmpty()) { return; }
		
		throw new UnitException("INCLUDE_NEW_RULE");
	}
	
	public void validateUnitFields(Unit unit) throws UnitException {
		Set<ConstraintViolation<Unit>> violations = validator.validate(unit);
		
		if (violations.isEmpty()) { return; }
		
		throw new UnitException("INCLUDE_NEW_RULE");
	}
	
}
