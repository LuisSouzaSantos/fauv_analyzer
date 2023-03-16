package com.fauv.analyzer.service;

import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.fauv.analyzer.entity.helper.FmImpactHelper;

public interface CsvHandleService {

	public Set<FmImpactHelper> parseCsvFileToImpactHelpers(MultipartFile csvFile) throws Exception;
	
}
