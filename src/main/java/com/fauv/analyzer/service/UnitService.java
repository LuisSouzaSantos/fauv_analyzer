package com.fauv.analyzer.service;

import java.util.List;

import com.fauv.analyzer.entity.Unit;
import com.fauv.analyzer.entity.dto.UnitDTO;
import com.fauv.analyzer.exception.UnitException;
import com.fauv.analyzer.form.UnitForm;

public interface UnitService {

	public Unit create(UnitForm form) throws UnitException;
	
	public Unit edit(Unit unit) throws UnitException;
	
	public Unit getById(Long id);
	
	public List<Unit> getAll(); 
	
	public UnitDTO toUnitDTO(Unit unit);
	
	public List<UnitDTO> toUnitDTO(List<Unit> list);
	
	public Unit toUnit(UnitDTO unitDTO);
	
}
