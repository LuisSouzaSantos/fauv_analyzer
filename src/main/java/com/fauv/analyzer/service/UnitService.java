package com.fauv.analyzer.service;

import java.util.List;

import com.fauv.analyzer.entity.Unit;
import com.fauv.analyzer.entity.dto.UnitDTO;
import com.fauv.analyzer.entity.form.UnitForm;
import com.fauv.analyzer.exception.EntityValidatorException;
import com.fauv.analyzer.exception.UnitException;

public interface UnitService {

	public Unit create(UnitForm form) throws UnitException, EntityValidatorException;
	
	public Unit edit(Unit unit) throws UnitException, EntityValidatorException;
	
	public Unit getById(Long id);
	
	public Unit getByIdValidateIt(Long id) throws UnitException;
	
	public List<Unit> getAll(); 
	
	public UnitDTO toUnitDTO(Unit unit);
	
	public List<UnitDTO> toUnitDTO(List<Unit> list);
	
	public Unit toUnit(UnitDTO unitDTO);
	
}
