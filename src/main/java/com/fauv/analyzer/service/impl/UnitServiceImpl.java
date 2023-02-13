package com.fauv.analyzer.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fauv.analyzer.entity.Unit;
import com.fauv.analyzer.entity.dto.UnitDTO;
import com.fauv.analyzer.exception.UnitException;
import com.fauv.analyzer.form.UnitForm;
import com.fauv.analyzer.message.UnitMessage;
import com.fauv.analyzer.repository.UnitRepository;
import com.fauv.analyzer.service.UnitService;
import com.fauv.analyzer.validator.UnitValidator;

@Service
public class UnitServiceImpl implements UnitService {

	@Autowired
	private UnitRepository unitRepository;
	
	@Autowired
	private UnitValidator unitValidator;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Unit create(UnitForm form) throws UnitException {
		unitValidator.validateAuthenticationFormFields(form);
		
		Unit duplicatedUnit = getByName(form.getName());
		
		if (duplicatedUnit != null) { throw new UnitException(UnitMessage.ERROR_DUPLICATE_BY_NAME); }
		
		Unit unit = new Unit();
		unit.setName(form.getName());
		unit.setActive(form.isActive());
		
		return unitRepository.save(unit);
	}

	@Override
	public Unit edit(Unit unit) throws UnitException {
		unitValidator.validateUnitFields(unit);
		
		Unit duplicatedUnit = getByName(unit.getName());
		
		if ((duplicatedUnit != null) && !(unit.getId().equals(duplicatedUnit.getId()))) { throw new UnitException(UnitMessage.ERROR_DUPLICATE_BY_NAME); }
		
		return unitRepository.save(unit);	
	}

	@Override
	public Unit getById(Long id) {		
		return unitRepository.findById(id).orElse(null);
	}
	
	@Override
	public List<Unit> getAll() {
		return unitRepository.findAll();
	}

	private Unit getByName(String name) {
		return unitRepository.findByName(name);
	}

	@Override
	public UnitDTO toUnitDTO(Unit unit) {
		if (unit == null) { return null; }
		
		return modelMapper.map(unit, UnitDTO.class);
	}

	@Override
	public Unit toUnit(UnitDTO unitDTO) {
		if (unitDTO == null) { return null; }
		
		return modelMapper.map(unitDTO, Unit.class);
	}

	@Override
	public List<UnitDTO> toUnitDTO(List<Unit> list) {
		if (list == null) { return null; }
		
		return list.stream().map(unit -> toUnitDTO(unit)).collect(Collectors.toList());
	}



}
