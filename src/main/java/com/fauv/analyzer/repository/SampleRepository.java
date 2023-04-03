package com.fauv.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fauv.analyzer.entity.Model;
import com.fauv.analyzer.entity.Sample;

@Repository
public interface SampleRepository extends JpaRepository<Sample, Long> {

	public Sample findByPinAndModel(String pin, Model model);
	
}
