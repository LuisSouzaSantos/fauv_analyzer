package com.fauv.analyzer.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fauv.analyzer.entity.Car;
import com.fauv.analyzer.entity.Equipment;
import com.fauv.analyzer.entity.Unit;
import com.fauv.analyzer.entity.dto.CarDTO;
import com.fauv.analyzer.entity.form.CarForm;
import com.fauv.analyzer.entity.form.update.UpdateCarForm;
import com.fauv.analyzer.exception.CarException;
import com.fauv.analyzer.exception.EntityValidatorException;
import com.fauv.analyzer.exception.EquipmentException;
import com.fauv.analyzer.exception.UnitException;
import com.fauv.analyzer.message.CarMessage;
import com.fauv.analyzer.message.EquipmentMessage;
import com.fauv.analyzer.repository.CarRepository;
import com.fauv.analyzer.service.CarService;
import com.fauv.analyzer.service.UnitService;
import com.fauv.analyzer.validator.EntityValidator;

@Service
public class CarServiceImpl implements CarService {

	@Autowired
	private CarRepository carRepository;
	
	@Autowired
	private EntityValidator<Car> carValidator;
	
	@Autowired
	private EntityValidator<CarForm> carFormValidator;
	
	@Autowired
	private UnitService unitService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Car create(CarForm form) throws CarException, EntityValidatorException, UnitException {
		carFormValidator.validateFields(form);
		
		Unit unit = unitService.getByIdValidateIt(form.getUnitId());
		
		Car duplicateCar = getByNameAndUnit(form.getName(), unit);
		
		if (duplicateCar != null) { throw new CarException(CarMessage.ERROR_DUPLICATE_BY_NAME); }
		
		Car car = new Car();
		car.setName(form.getName());
		car.setActive(form.isActive());
		car.setUnit(unit);
		
		return carRepository.save(car);
	}

	@Override
	public Car edit(Car car) throws CarException, EntityValidatorException, UnitException {
		carValidator.validateFields(car);
		
		Unit unit = unitService.getByIdValidateIt(car.getUnit().getId());
		
		Car carById = getByIdValidateIt(car.getId());
		Car duplicateCar = getByNameAndUnit(car.getName(), unit);
		
		if ((duplicateCar != null) && !(carById.getId().equals(duplicateCar.getId()))) { throw new CarException(CarMessage.ERROR_DUPLICATE_BY_NAME); }

		carById.setName(car.getName());
		carById.setActive(car.isActive());
		
		if (!(car.getUnit().getId().equals(carById.getUnit().getId()))) {
			carById.setUnit(unit);
		}
			
		return carRepository.save(carById);
	}

	@Override
	public Car getById(Long id) {
		return carRepository.findById(id).orElse(null);
	}

	@Override
	public Car getByIdValidateIt(Long id) throws CarException {
		return carRepository.findById(id).orElseThrow(() -> new CarException(CarMessage.ERROR_NOT_FOUND));
	}

	@Override
	public List<Car> getAll() {
		return carRepository.findAll();
	}

	@Override
	public CarDTO toCarDTO(Car car) {
		if (car == null) { return null; }
		
		return modelMapper.map(car, CarDTO.class);
	}

	@Override
	public List<CarDTO> toCarDTO(List<Car> list) {
		return list.stream().map(car -> toCarDTO(car)).collect(Collectors.toList());
	}

	@Override
	public Car toCar(CarDTO carDTO) {
		if (carDTO == null) { return null; }
		
		return modelMapper.map(carDTO, Car.class);
	}
	
	@Override
	public Car toCar(UpdateCarForm updateCarForm) {
		if (updateCarForm == null) { return null; }
		
		Car car = modelMapper.map(updateCarForm, Car.class);
		car.setUnit(new Unit());
		car.getUnit().setId(updateCarForm.getUnitId());
		
		return car;
	}

	private Car getByNameAndUnit(String name, Unit unit) {
		return carRepository.findByNameAndUnit(name, unit);
	}
	
}
