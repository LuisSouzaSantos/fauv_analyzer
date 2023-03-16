package com.fauv.analyzer.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fauv.analyzer.entity.Model;
import com.fauv.analyzer.entity.form.ModelForm;
import com.fauv.analyzer.entity.form.ModelPreview;
import com.fauv.analyzer.exception.EntityValidatorException;
import com.fauv.analyzer.exception.ModelException;

public interface ModelService {

	public ModelPreview preview(MultipartFile dmoFile, MultipartFile csvFile) throws Exception;
	
	public Model create(ModelForm form) throws EntityValidatorException, ModelException;
	
	public Model edit(Model model) throws EntityValidatorException, ModelException;
	
	public Model getById(Long id);
	
	public Model getByIdValidateIt(Long id) throws ModelException;
	
	public List<Model> getAll(); 
	
}
