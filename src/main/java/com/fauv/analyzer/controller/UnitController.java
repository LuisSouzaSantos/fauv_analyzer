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

import com.fauv.analyzer.entity.Unit;
import com.fauv.analyzer.entity.dto.UnitDTO;
import com.fauv.analyzer.entity.form.UnitForm;
import com.fauv.analyzer.exception.UnitException;
import com.fauv.analyzer.service.UnitService;

@CrossOrigin
@RestController
@RequestMapping("/unit")
public class UnitController {

	@Autowired
	private UnitService unitService;
	
	@PostMapping
	public ResponseEntity<UnitDTO> create(@RequestBody UnitForm form) throws UnitException {
		Unit createdUnit = unitService.create(form);
		
		return ResponseEntity.ok(unitService.toUnitDTO(createdUnit));
	}
	
	@PutMapping
	public ResponseEntity<UnitDTO> update(@RequestBody UnitDTO unitDTO) throws UnitException {
		Unit unitToBeUpdate = unitService.edit(unitService.toUnit(unitDTO));
		
		return ResponseEntity.ok(unitService.toUnitDTO(unitToBeUpdate));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UnitDTO> getById(@RequestParam Long id) throws UnitException {
		Unit unit = unitService.getById(id);
		
		return ResponseEntity.ok(unitService.toUnitDTO(unit));
	}
	
	@GetMapping
	public ResponseEntity<List<UnitDTO>> getAll() {
		List<Unit> units = unitService.getAll();
		
		return ResponseEntity.ok(unitService.toUnitDTO(units));
	}
	
	
}
