package com.fauv.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fauv.analyzer.entity.Unit;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

	public Unit findByName(String name);
	
}
