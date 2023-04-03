package com.fauv.analyzer.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fauv.analyzer.entity.Sample;
import com.fauv.analyzer.entity.dto.SampleDTO;
import com.fauv.analyzer.exception.EquipmentException;
import com.fauv.analyzer.exception.ModelException;
import com.fauv.analyzer.exception.SampleException;
import com.fauv.analyzer.exception.UnitException;

public interface SampleService {

	public Sample save(MultipartFile dmoFile, Long unitId) throws UnitException, EquipmentException, ModelException, SampleException;
	
	public void delete(Long id);
	
	public List<Sample> getAll();
	
	public Sample getById(Long id);
	
	public Sample getByIdValidateIt(Long id) throws SampleException;
	
	public SampleDTO toSampleDTO(Sample sample);
	
	public List<SampleDTO> toSampleDTO(List<Sample> list);
		
}
