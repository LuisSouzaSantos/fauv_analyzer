package com.fauv.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fauv.analyzer.entity.Car;
import com.fauv.analyzer.entity.Unit;

public interface CarRepository extends JpaRepository<Car, Long> {

	public Car findByNameAndUnit(String name, Unit unit);
	
}
