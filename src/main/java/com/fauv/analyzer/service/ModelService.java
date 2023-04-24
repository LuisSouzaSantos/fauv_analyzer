package com.fauv.analyzer.service;

import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.fauv.analyzer.entity.Car;
import com.fauv.analyzer.entity.Model;
import com.fauv.analyzer.entity.Unit;
import com.fauv.analyzer.entity.form.ModelForm;
import com.fauv.analyzer.entity.form.ModelPreview;
import com.fauv.analyzer.exception.CarException;
import com.fauv.analyzer.exception.EntityValidatorException;
import com.fauv.analyzer.exception.ModelException;

public interface ModelService {

	public ModelPreview preview(MultipartFile dmoFile, MultipartFile csvFile) throws Exception;
	
	public Model create(ModelForm form) throws EntityValidatorException, ModelException, CarException;
	
	public Model edit(Model model) throws EntityValidatorException, ModelException, CarException;
	
	public void delete(Long id);
	
	public Model getById(Long id);
	
	public Model getByIdValidateIt(Long id) throws ModelException;
	
	public Model getByPartNumberAndCar(String partNumber, Car car);
	
	public Model getByPartNumberAndUnit(String partNumber, Unit unit);
	
	public Model getByPartNumberAndUnitValidateIt(String partNumber,Unit unit) throws ModelException;
	
	public Set<Model> getAllModelsByUnitId(Long unitId);
	
	public List<Model> getAll(); 
	
}
