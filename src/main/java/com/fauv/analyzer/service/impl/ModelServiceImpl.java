package com.fauv.analyzer.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fauv.analyzer.entity.Car;
import com.fauv.analyzer.entity.FmImpact;
import com.fauv.analyzer.entity.Model;
import com.fauv.analyzer.entity.NominalAxisCoordinate;
import com.fauv.analyzer.entity.NominalFm;
import com.fauv.analyzer.entity.NominalPmp;
import com.fauv.analyzer.entity.Unit;
import com.fauv.analyzer.entity.dto.PmpDTO;
import com.fauv.analyzer.entity.form.FmForm;
import com.fauv.analyzer.entity.form.FmImpactForm;
import com.fauv.analyzer.entity.form.ModelForm;
import com.fauv.analyzer.entity.form.ModelPreview;
import com.fauv.analyzer.entity.form.PmpForm;
import com.fauv.analyzer.entity.helper.FmHelper;
import com.fauv.analyzer.entity.helper.FmImpactHelper;
import com.fauv.analyzer.entity.helper.PmpHelper;
import com.fauv.analyzer.entity.helper.SampleHelper;
import com.fauv.analyzer.exception.CarException;
import com.fauv.analyzer.exception.EntityValidatorException;
import com.fauv.analyzer.exception.ModelException;
import com.fauv.analyzer.message.ModelMessage;
import com.fauv.analyzer.repository.ModelRepository;
import com.fauv.analyzer.service.CarService;
import com.fauv.analyzer.service.CsvHandleService;
import com.fauv.analyzer.service.ModelService;
import com.fauv.analyzer.service.ParserHandleService;
import com.fauv.analyzer.service.http.ParserHttp;
import com.fauv.analyzer.validator.ModelValidator;

@Service
public class ModelServiceImpl implements ModelService {

	@Autowired
	private ParserHttp parserHttp;
	
	@Autowired
	private CsvHandleService csvHandleService;
	
	@Autowired
	private ParserHandleService parserHandleService;
	
	@Autowired
	private ModelRepository modelRepository;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private ModelValidator modelValidator;
	
	@Override
	public ModelPreview preview(MultipartFile dmoFile, MultipartFile csvFile) throws Exception {
		SampleHelper sample = parserHttp.readDmoFileAndBuildASample(dmoFile);
		Set<FmImpactHelper> extractedFmImpactList = csvHandleService.parseCsvFileToImpactHelpers(csvFile);
				
		return createModelPreviewUsignSampleHelperAsReference(sample, extractedFmImpactList);
	}

	@Override
	public Model create(ModelForm form) throws EntityValidatorException, ModelException, CarException {
		modelValidator.validateModelForm(form);
		
		Car car = carService.getByIdValidateIt(form.getCar().getId());
		
		Model model = new Model();
		model.setPartNumber(form.getPartNumber());
		model.setStepDescription(form.getStepDescription());
		model.setCar(car);
		
		if (getByPartNumberAndCar(model.getPartNumber(), car) != null) { throw new ModelException(ModelMessage.DUPLICATE); }
		
		for (PmpForm pmpForm : form.getPmpList()) {
			NominalPmp nominalPmp = NominalPmp.buildNominalPmp(pmpForm);
			nominalPmp.setModel(model);
			
			modelValidator.validateNominalPmp(nominalPmp);
			model.getPmpList().add(nominalPmp);
		}
		
		for (FmForm fmForm: form.getFmList()) {			
			NominalFm nominalFm = NominalFm.buildNominalFm(fmForm);
			
			for (PmpDTO pmpDTO : fmForm.getPointsUsingToMap()) {
				NominalPmp nominalPmp = model.getPmpByName(pmpDTO.getName());
				
				if (nominalPmp == null) {
					System.out.println("NOT FOUND THE PMP BY NAME");
					continue;
				}
				
				nominalFm.getPointsUsingToMap().add(nominalPmp);
			}
			
			for (FmImpactForm fmImpactForm : fmForm.getFmImpactList()) {
				FmImpact fmImpact = FmImpact.buildFmImpact(fmImpactForm);
				fmImpact.setNominalFm(nominalFm);
				
				nominalFm.getFmImpactList().add(fmImpact);
			}
			
			nominalFm.setModel(model);
			
			modelValidator.validateNominalFm(nominalFm);
			model.getFmList().add(nominalFm);
		}
		
		modelValidator.validateModel(model);
		
		return modelRepository.save(model);
	}

	@Override
	public Model edit(Model model) throws EntityValidatorException, ModelException, CarException {
		Model modelById = getByIdValidateIt(model.getId());

		Car car = carService.getByIdValidateIt(model.getCar().getId());

		
		modelById.setPartNumber(model.getPartNumber());
		modelById.setStepDescription(model.getStepDescription());
		modelById.setCar(car);
		
		Model duplicateModel = getByPartNumberAndCar(model.getPartNumber(), model.getCar());
		
		if (duplicateModel != null && !modelById.getId().equals(duplicateModel.getId())) { throw new ModelException(ModelMessage.DUPLICATE); }
		
		for (NominalPmp previousNominalPmp : model.getPmpList()) {			
			NominalPmp nominalPmp = previousNominalPmp.getId() == null ? NominalPmp.buildNominalPmp(previousNominalPmp, model):previousNominalPmp;
			
			if (nominalPmp.getId() != null) {
				nominalPmp.setName(previousNominalPmp.getName());
				nominalPmp.setAxis(previousNominalPmp.getAxis());
				nominalPmp.setActive(previousNominalPmp.isActive());
				nominalPmp.setX(previousNominalPmp.getX());
				nominalPmp.setY(previousNominalPmp.getY());
				nominalPmp.setZ(previousNominalPmp.getZ());
				
				redoNewNominalAxisCoordinate(nominalPmp.getAxisCoordinateList(), nominalPmp);
			}
			
			modelValidator.validateNominalPmp(nominalPmp);
			model.getPmpList().add(nominalPmp);
		}
		
		for (NominalFm previousNominalFm: model.getFmList()) {
			NominalFm nominalFm = previousNominalFm.getId() == null ? NominalFm.buildNominalFm(previousNominalFm, model):previousNominalFm;
			
			nominalFm.getPointsUsingToMap().clear();
			nominalFm.getFmImpactList().clear();
			
			for (NominalPmp previousNominalPmp : nominalFm.getPointsUsingToMap()) {
				NominalPmp nominalPmp = model.getPmpByName(previousNominalPmp.getName());
				
				if (nominalPmp == null) {
					System.out.println("NOT FOUND THE PMP BY NAME");
					continue; 
				}
		
				nominalFm.getPointsUsingToMap().add(nominalPmp);
			}
			
			for (FmImpact previousfmImpact : nominalFm.getFmImpactList()) {
				FmImpact fmImpact = previousfmImpact.getId() == null ? FmImpact.buildFmImpact(previousfmImpact, nominalFm):previousfmImpact;
				
				modelValidator.validateFmImpact(fmImpact);
				
				nominalFm.getFmImpactList().add(fmImpact);
			}
			
			modelValidator.validateNominalFm(nominalFm);
			
			model.getFmList().add(nominalFm);
		}
		
		
		return modelRepository.save(modelById);
	}

	@Override
	public Model getById(Long id) {
		return modelRepository.findById(id).orElse(null);
	}

	@Override
	public Model getByIdValidateIt(Long id) throws ModelException {
		Model model = getById(id);
		
		if (model == null) { throw new ModelException(ModelMessage.NOT_FOUND); }
		
		return model;
	}

	@Override
	public List<Model> getAll() {
		return modelRepository.findAll();
	}
	
	@Override
	public Model getByPartNumberAndCar(String partNumber, Car car) {
		return modelRepository.findByPartNumberAndCar(partNumber, car);
	}
	
	@Override
	public Model getByPartNumberAndUnit(String partNumber, Unit unit) {
		return modelRepository.findByPartNumberAndCarUnit(partNumber, unit);
	}
	
	@Override
	public Model getByPartNumberAndUnitValidateIt(String partNumber, Unit unit) throws ModelException {
		Model model = getByPartNumberAndUnit(partNumber, unit);
		
		if (model == null) { throw new ModelException(ModelMessage.NOT_FOUND); }
		
		return model;
	}
	
	@Override
	public void delete(Long id) {
		modelRepository.deleteById(id);
	}

	private ModelPreview createModelPreviewUsignSampleHelperAsReference(SampleHelper sampleHelper, Set<FmImpactHelper> extractedFmImpactList) {
		ModelPreview modelForm = new ModelPreview();
		
		List<PmpDTO> pmpDTOList = parserHandleService.buildPmpDTOBasedOnHelper(sampleHelper.getPmpList());
		
		modelForm.setPartNumber(sampleHelper.getHeader().getPartNumber());
		modelForm.setStepDescription(null);
		modelForm.setCar(null);
		modelForm.setPmpList(buildPmpFormSet(sampleHelper.getPmpList()));
		modelForm.setFmList(buildFmFormSet(sampleHelper.getFmList(), pmpDTOList, extractedFmImpactList));
		
		return modelForm;
	}
	
	private Set<PmpForm> buildPmpFormSet(List<PmpHelper> helperList) {
		return parserHandleService.buildPmpFormBasedOnHelper(helperList).stream().collect(Collectors.toSet());
	}
	
	private Set<FmForm> buildFmFormSet(List<FmHelper> helperList, List<PmpDTO> pmpDTOList, Set<FmImpactHelper> extractedFmImpactList) {
		Set<FmForm> setFmForm = new HashSet<>();
		
		for (FmHelper fmHelper : helperList) {			
			List<String> pointsNameToBeFound = fmHelper.getPmpNameList();			
			
			List<PmpDTO> pointsFound = pmpDTOList.stream()
					.filter(pmpDTO -> pointsNameToBeFound.contains(pmpDTO.getName())).collect(Collectors.toList());			
			
			FmImpactHelper fmImpactHelperFound =  extractedFmImpactList.stream()
					.filter(fmImpactHelper -> fmImpactHelper.getFmName().equals(fmHelper.getName())).findFirst().orElse(null);
			
			FmForm fmForm = parserHandleService.buildFmFormBasedOnHelper(fmHelper);
			fmForm.setPointsUsingToMap(pointsFound);
			
			if (fmImpactHelperFound != null) {
				fmForm.setFmImpactList(parserHandleService.buildFmImpactFormBasedOnHelper(fmImpactHelperFound));
			}
			
			setFmForm.add(fmForm);
		}
		
		return setFmForm;
	}
	
	// Review this method
	private List<NominalAxisCoordinate> redoNewNominalAxisCoordinate(List<NominalAxisCoordinate> axisCoordinateListToBeUpdate, NominalPmp nominalPmp) throws ModelException, EntityValidatorException {
		
		for (NominalAxisCoordinate axisCoordinateToBeUpdate : axisCoordinateListToBeUpdate) {
			
			//Find NominalAxisCoordinate by name
//			NominalAxisCoordinate currentNominalAxisCoordinate = currentNominalAxisCoordinateList.stream()
//					.filter(axisCoordinate -> axisCoordinate.getName().equals(axisCoordinateToBeUpdate.getName()))
//					.findFirst().orElse(null);		
//			
//			if ((currentNominalAxisCoordinate != null) && (axisCoordinateToBeUpdate.getId() == null)) { throw new ModelException(ModelMessage.THIS_NOMINAL_AXIS_COORDINATE_CANNOT_HAS_ID_NULL); }
//			if ((currentNominalAxisCoordinate != null) && !(axisCoordinateToBeUpdate.getId().equals(currentNominalAxisCoordinate.getId()))) { throw new ModelException(ModelMessage.NOMINAL_AXIS_COORDINATE_NOT_FIND_BY_NAME_IN_UPDATE); }
			if (axisCoordinateToBeUpdate.getId() == null) { 
				axisCoordinateToBeUpdate.setNominalPmp(nominalPmp); 
			}
			
			modelValidator.validateNominalAxisCoordinate(axisCoordinateToBeUpdate);
		}
		
		return axisCoordinateListToBeUpdate;
	}
		
}
