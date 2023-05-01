package com.fauv.analyzer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fauv.analyzer.entity.Sample;
import com.fauv.analyzer.entity.dto.SampleDTO;
import com.fauv.analyzer.entity.dto.SampleLoadingDTO;
import com.fauv.analyzer.exception.SampleException;
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
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		sampleService.delete(id);
	}
	
	@GetMapping
	public ResponseEntity<List<SampleLoadingDTO>> getAll() {
		List<Sample> sampleList = sampleService.getAll();
		
		return ResponseEntity.ok(sampleService.toSampleLoadingDTO(sampleList)); 
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<SampleDTO> getById(@PathVariable Long id) throws SampleException {
		Sample sample = sampleService.getByIdValidateIt(id);
		
		return ResponseEntity.ok(sampleService.toSampleDTO(sample)); 
	}
	
}
