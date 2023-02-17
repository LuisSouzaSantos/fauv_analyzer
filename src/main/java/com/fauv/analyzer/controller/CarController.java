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

import com.fauv.analyzer.entity.Car;
import com.fauv.analyzer.entity.dto.CarDTO;
import com.fauv.analyzer.entity.form.CarForm;
import com.fauv.analyzer.entity.form.update.UpdateCarForm;
import com.fauv.analyzer.exception.CarException;
import com.fauv.analyzer.exception.EntityValidatorException;
import com.fauv.analyzer.exception.UnitException;
import com.fauv.analyzer.service.CarService;

@CrossOrigin
@RestController
@RequestMapping("/car")
public class CarController {

	@Autowired
	private CarService carService;
	
	@PostMapping
	public ResponseEntity<CarDTO> create(@RequestBody CarForm form) throws UnitException, EntityValidatorException, CarException {
		Car createdCar = carService.create(form);
		
		return ResponseEntity.ok(carService.toCarDTO(createdCar));
	}
	
	@PutMapping
	public ResponseEntity<CarDTO> edit(@RequestBody UpdateCarForm form) throws UnitException, EntityValidatorException, CarException {
		Car updatedCar = carService.edit(carService.toCar(form));
		
		return ResponseEntity.ok(carService.toCarDTO(updatedCar));
	}
	
	@GetMapping
	public ResponseEntity<List<CarDTO>> getAll() {
		return  ResponseEntity.ok(carService.toCarDTO(carService.getAll()));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CarDTO> getById(@RequestParam Long id) throws CarException {
		Car car = carService.getByIdValidateIt(id);
		
		return ResponseEntity.ok(carService.toCarDTO(car));
	}
	
}
