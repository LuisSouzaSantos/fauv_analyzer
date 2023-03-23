package com.fauv.analyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fauv.analyzer.entity.Sample;
import com.fauv.analyzer.entity.dto.SampleDTO;
import com.fauv.analyzer.service.SampleService;

@CrossOrigin
@RestController
@RequestMapping("/sample")
public class SampleController {

	@Autowired
	private SampleService sampleService;
	
	@PostMapping
	public ResponseEntity<SampleDTO> upload(@RequestParam("dmoFile") MultipartFile dmoFile) throws Exception {
		Sample sample = sampleService.save(dmoFile, 1L);
		
		return ResponseEntity.ok(sampleService.toSampleDTO(sample)); 
	}
	
}
