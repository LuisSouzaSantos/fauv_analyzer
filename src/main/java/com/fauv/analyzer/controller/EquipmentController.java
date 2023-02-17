package com.fauv.analyzer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fauv.analyzer.entity.Equipment;
import com.fauv.analyzer.entity.dto.EquipmentDTO;
import com.fauv.analyzer.entity.form.EquipmentForm;
import com.fauv.analyzer.entity.form.update.UpdateEquipmentForm;
import com.fauv.analyzer.exception.EntityValidatorException;
import com.fauv.analyzer.exception.EquipmentException;
import com.fauv.analyzer.exception.UnitException;
import com.fauv.analyzer.service.EquipmentService;

@CrossOrigin
@RestController
@RequestMapping("/equipment")
public class EquipmentController {

	@Autowired
	private EquipmentService equipmentService;
	
	@PostMapping
	public ResponseEntity<EquipmentDTO> create(@RequestBody EquipmentForm form) throws UnitException, EquipmentException, EntityValidatorException {
		Equipment createdEquipment = equipmentService.create(form);
		
		return ResponseEntity.ok(equipmentService.toEquipmentDTO(createdEquipment));
	}
	
	@PutMapping
	public ResponseEntity<EquipmentDTO> edit(@RequestBody UpdateEquipmentForm form) throws UnitException, EquipmentException, EntityValidatorException {
		Equipment updatedEquipment = equipmentService.edit(equipmentService.toEquipment(form));
		
		return ResponseEntity.ok(equipmentService.toEquipmentDTO(updatedEquipment));
	}
	
	@GetMapping
	public ResponseEntity<List<EquipmentDTO>> getAll() {
		return  ResponseEntity.ok(equipmentService.toEquipmentDTO(equipmentService.getAll()));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<EquipmentDTO> getById(@RequestParam Long id) {
		Equipment equipment = equipmentService.getById(id);
		
		return  ResponseEntity.ok(equipmentService.toEquipmentDTO(equipment));
	}
	
}
