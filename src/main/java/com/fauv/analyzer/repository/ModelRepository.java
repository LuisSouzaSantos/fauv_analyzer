package com.fauv.analyzer.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fauv.analyzer.entity.Car;
import com.fauv.analyzer.entity.Model;
import com.fauv.analyzer.entity.Unit;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {

	public Model findByPartNumberAndCar(String partNumber,Car car);
	
	public Model findByPartNumberAndCarUnit(String partNumber, Unit unit);
	
	@Query(value = "SELECT m FROM Model m WHERE m.car.unit.id = :unitId ")
	public Set<Model> findAllPartNumbersByUnitId(Long unitId);
	
}
