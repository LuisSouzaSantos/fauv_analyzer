package com.fauv.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fauv.analyzer.entity.Equipment;
import com.fauv.analyzer.entity.Unit;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long>  {

	public Equipment findByNameAndUnit(String name, Unit unit);
	
}
