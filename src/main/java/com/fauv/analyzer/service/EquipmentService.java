package com.fauv.analyzer.service;

import java.util.List;

import com.fauv.analyzer.entity.Equipment;
import com.fauv.analyzer.entity.Unit;
import com.fauv.analyzer.entity.dto.EquipmentDTO;
import com.fauv.analyzer.entity.form.EquipmentForm;
import com.fauv.analyzer.entity.form.update.UpdateEquipmentForm;
import com.fauv.analyzer.exception.EntityValidatorException;
import com.fauv.analyzer.exception.EquipmentException;
import com.fauv.analyzer.exception.UnitException;

public interface EquipmentService {

	public Equipment create(EquipmentForm form) throws EquipmentException, EntityValidatorException, UnitException;
	
	public Equipment edit(Equipment equipment) throws EquipmentException, EntityValidatorException, UnitException;
	
	public Equipment getById(Long id);
	
	public Equipment getByNameAndUnit(String name, Unit unit);
	
	public Equipment getByNameAndUnitValidateIt(String name, Unit unit) throws EquipmentException;
	
	public Equipment getByIdValidateIt(Long id) throws EquipmentException;
	
	public List<Equipment> getAll(); 
	
	public EquipmentDTO toEquipmentDTO(Equipment equipment);
	
	public List<EquipmentDTO> toEquipmentDTO(List<Equipment> list);
	
	public Equipment toEquipment(EquipmentDTO equipmentDTO);
	
	public Equipment toEquipment(UpdateEquipmentForm updateEquipmentForm);
	
}
