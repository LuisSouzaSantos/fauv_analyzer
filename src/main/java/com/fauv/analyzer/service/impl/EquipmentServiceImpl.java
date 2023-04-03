package com.fauv.analyzer.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fauv.analyzer.entity.Equipment;
import com.fauv.analyzer.entity.Unit;
import com.fauv.analyzer.entity.dto.EquipmentDTO;
import com.fauv.analyzer.entity.form.EquipmentForm;
import com.fauv.analyzer.entity.form.update.UpdateEquipmentForm;
import com.fauv.analyzer.exception.EntityValidatorException;
import com.fauv.analyzer.exception.EquipmentException;
import com.fauv.analyzer.exception.UnitException;
import com.fauv.analyzer.message.EquipmentMessage;
import com.fauv.analyzer.repository.EquipmentRepository;
import com.fauv.analyzer.service.EquipmentService;
import com.fauv.analyzer.service.UnitService;
import com.fauv.analyzer.validator.EntityValidator;

@Service
public class EquipmentServiceImpl implements EquipmentService {

	@Autowired
	private EquipmentRepository equipmentRepository;
	
	@Autowired
	private EntityValidator<EquipmentForm> equipmentFormValidator;
	
	@Autowired
	private EntityValidator<Equipment> equipmentValidator;
	
	@Autowired
	private UnitService unitService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Equipment create(EquipmentForm form) throws EquipmentException, EntityValidatorException, UnitException {
		equipmentFormValidator.validateFields(form);
		
		Unit unit = unitService.getByIdValidateIt(form.getUnitId());
		
		Equipment equipmentDuplicate = getByNameAndUnit(form.getName(), unit);
		
		if (equipmentDuplicate != null) { throw new EquipmentException(EquipmentMessage.ERROR_DUPLICATE_BY_NAME); }
		
		Equipment equipment = new Equipment();
		equipment.setName(form.getName());
		equipment.setActive(form.isActive());
		equipment.setUnit(unit);
		
		return equipmentRepository.save(equipment);
	}

	@Override
	public Equipment edit(Equipment equipment) throws EquipmentException, EntityValidatorException, UnitException {
		equipmentValidator.validateFields(equipment);
		
		Unit unit = unitService.getByIdValidateIt(equipment.getUnit().getId());
		
		Equipment equipmentById = getByIdValidateIt(equipment.getId());
		Equipment equipmentDuplicate = getByNameAndUnit(equipment.getName(), unit);
		
		if ((equipmentDuplicate != null) && !(equipmentById.getId().equals(equipmentDuplicate.getId()))) { throw new EquipmentException(EquipmentMessage.ERROR_DUPLICATE_BY_NAME); }

		equipmentById.setName(equipment.getName());
		equipmentById.setActive(equipment.isActive());
		
		if (!(equipment.getUnit().getId().equals(equipmentById.getUnit().getId()))) {
			equipmentById.setUnit(unit);
		}
			
		return equipmentRepository.save(equipmentById);
	}

	@Override
	public Equipment getById(Long id) {		
		return equipmentRepository.findById(id).orElse(null);
	}
	
	@Override
	public Equipment getByIdValidateIt(Long id) throws EquipmentException {
		return equipmentRepository.findById(id).orElseThrow(() -> new EquipmentException(EquipmentMessage.ERROR_NOT_FOUND));
	}

	@Override
	public List<Equipment> getAll() {
		return equipmentRepository.findAll();
	}

	@Override
	public EquipmentDTO toEquipmentDTO(Equipment equipment) {
		if (equipment == null) { return null; }
	
		return modelMapper.map(equipment, EquipmentDTO.class);
	}

	@Override
	public List<EquipmentDTO> toEquipmentDTO(List<Equipment> list) {
		if (list == null) { return null; }
		
		return list.stream().map(equipment -> toEquipmentDTO(equipment)).collect(Collectors.toList());
	}

	@Override
	public Equipment toEquipment(EquipmentDTO equipmentDTO) {
		if (equipmentDTO == null) { return null; }
		
		return modelMapper.map(equipmentDTO, Equipment.class);
	}
	
	@Override
	public Equipment toEquipment(UpdateEquipmentForm updateEquipmentForm) {
		if (updateEquipmentForm == null) { return null; }
		
		Equipment equipment = modelMapper.map(updateEquipmentForm, Equipment.class);
		equipment.setUnit(new Unit());
		equipment.getUnit().setId(updateEquipmentForm.getUnitId());
		
		return equipment;
	}
	
	@Override
	public Equipment getByNameAndUnit(String name, Unit unit) {
		return equipmentRepository.findByNameAndUnit(name, unit);
	}

	@Override
	public Equipment getByNameAndUnitValidateIt(String name, Unit unit) throws EquipmentException {
		Equipment equipment = getByNameAndUnit(name, unit);
		
		if (equipment == null) { throw new EquipmentException(EquipmentMessage.ERROR_NOT_FOUND); }
		
		return equipment;
	}
 
}
