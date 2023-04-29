package com.fauv.analyzer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fauv.analyzer.entity.Model;
import com.fauv.analyzer.entity.dto.ModelDTO;
import com.fauv.analyzer.entity.form.ModelForm;
import com.fauv.analyzer.entity.form.ModelPreview;
import com.fauv.analyzer.exception.CarException;
import com.fauv.analyzer.exception.EntityValidatorException;
import com.fauv.analyzer.exception.ModelException;
import com.fauv.analyzer.service.ModelHelperService;
import com.fauv.analyzer.service.ModelService;

@CrossOrigin
@RestController
@RequestMapping("/model")
public class ModelController {

	@Autowired
	private ModelService modelService;
	
	@Autowired
	private ModelHelperService modelHelperService;
	
	@PostMapping("/preview")
	public ResponseEntity<ModelPreview> previewModel(@RequestParam(name = "dmoFile", required = true) MultipartFile dmoFile, @RequestParam(name = "csvFile", required = false) MultipartFile csvFile) throws Exception {
		return ResponseEntity.ok(modelService.preview(dmoFile, csvFile)); 
	}
	
	@PostMapping
	public ResponseEntity<ModelDTO> createModel(@RequestBody ModelForm form) throws Exception {
		Model model = modelService.create(form);
		
		return ResponseEntity.ok(modelHelperService.toModelDTO(model)); 
	}
	
	@GetMapping
	public ResponseEntity<List<ModelDTO>> getAll() throws ModelException {
		List<Model> models = modelService.getAll();
		
		return ResponseEntity.ok(modelHelperService.toModelDTO(models)); 
	}
	
	@PutMapping
	public ResponseEntity<ModelDTO> edit(@RequestBody ModelDTO modelDTO) throws EntityValidatorException, ModelException, CarException {
		Model modelToBeSave = modelHelperService.toModel(modelDTO);
		
		Model model = modelService.edit(modelToBeSave);
		
		return ResponseEntity.ok(modelHelperService.toModelDTO(model)); 
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ModelDTO> getById(@PathVariable Long id) throws ModelException {
		Model model = modelService.getByIdValidateIt(id);
		
		return ResponseEntity.ok(modelHelperService.toModelDTO(model)); 
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		modelService.delete(id);
	}
	
	
	
}
