package com.fauv.analyzer.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fauv.analyzer.entity.MeasurementFm;
import com.fauv.analyzer.entity.Model;
import com.fauv.analyzer.entity.Sample;

@Repository
public interface SampleRepository extends JpaRepository<Sample, Long> {

	public Sample findByPinAndModel(String pin, Model model);
	
	public Set<Sample> findByModelIn(Set<Model> models);

	@Query(value = " SELECT mf FROM Sample s "
			+ " INNER JOIN s.measurementFmList mf "
			+ " where mf.nominalFm.name = :fmName and s.model.id = :modelId "
			+ " order by s.scanInitDate "
			+ " ")
	public List<MeasurementFm> findMeasurementFmListBasedOnModelAndFmName(Long modelId, String fmName);
	
}
