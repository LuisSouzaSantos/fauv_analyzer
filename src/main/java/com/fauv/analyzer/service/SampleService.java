package com.fauv.analyzer.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fauv.analyzer.entity.Sample;
import com.fauv.analyzer.entity.dto.SampleDTO;
import com.fauv.analyzer.exception.UnitException;

public interface SampleService {

	public Sample save(MultipartFile dmoFile, Long unitId) throws UnitException;
	
	public List<Sample> getAll();
	
	public Sample getById(Long id);
	
	public SampleDTO toSampleDTO(Sample sample);
	
	public List<SampleDTO> toSampleDTO(List<Sample> list);
		
}
