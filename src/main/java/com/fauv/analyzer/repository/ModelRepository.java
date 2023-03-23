package com.fauv.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fauv.analyzer.entity.Car;
import com.fauv.analyzer.entity.Model;
import com.fauv.analyzer.entity.Unit;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {

	public Model findByPartNumberAndCar(String partNumber,Car car);
	
	public Model findByPartNumberAndCarUnit(String partNumber, Unit unit);
	
}
