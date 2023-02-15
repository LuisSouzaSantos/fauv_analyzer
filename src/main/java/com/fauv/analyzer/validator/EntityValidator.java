package com.fauv.analyzer.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fauv.analyzer.exception.EntityValidatorException;

@Service
public class EntityValidator<T> {

	@Autowired
	private Validator validator;
	
	public void validateFields(T entityValidatorFields) throws EntityValidatorException {
		Set<ConstraintViolation<T>> violations = validator.validate(entityValidatorFields);
		
		if (violations.isEmpty()) { return; }
		
		throw new EntityValidatorException("INCLUDE_NEW_RULE");
	}
	
}
