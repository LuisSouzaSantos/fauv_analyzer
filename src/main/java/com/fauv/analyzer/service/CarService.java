package com.fauv.analyzer.service;

import java.util.List;

import com.fauv.analyzer.entity.Car;
import com.fauv.analyzer.entity.dto.CarDTO;
import com.fauv.analyzer.entity.form.CarForm;
import com.fauv.analyzer.entity.form.update.UpdateCarForm;
import com.fauv.analyzer.exception.CarException;
import com.fauv.analyzer.exception.EntityValidatorException;
import com.fauv.analyzer.exception.UnitException;

public interface CarService {

	public Car create(CarForm form) throws CarException, EntityValidatorException, UnitException;
	
	public Car edit(Car car) throws CarException, EntityValidatorException, UnitException;
	
	public Car getById(Long id);
	
	public Car getByIdValidateIt(Long id) throws CarException;
	
	public List<Car> getAll(); 
	
	public CarDTO toCarDTO(Car car);
	
	public List<CarDTO> toCarDTO(List<Car> list);
	
	public Car toCar(CarDTO carDTO);
	
	public Car toCar(UpdateCarForm updateCarForm);
	
}
