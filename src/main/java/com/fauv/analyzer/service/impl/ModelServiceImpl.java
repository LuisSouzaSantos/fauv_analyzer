package com.fauv.analyzer.service.impl;

import java.util.ArrayList;
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
		Set<FmImpactHelper> extractedFmImpactList = new HashSet<>();
		
		if (csvFile != null) { extractedFmImpactList = csvHandleService.parseCsvFileToImpactHelpers(csvFile); }
						
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
			
			for (PmpDTO pmpDTO : fmForm.getPmpList()) {
				NominalPmp nominalPmp = model.getPmpByName(pmpDTO.getName());
				
				if (nominalPmp == null) { continue; }
				
				nominalFm.getPmpList().add(nominalPmp);
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
		
		List<NominalPmp> deletedPmps = getDeletedNominalPmp(modelById.getPmpList(), model.getPmpList());
		List<NominalFm> deletedFms = getDeletedNominalFm(modelById.getFmList(), model.getFmList());
		
		checkDeletedNominalPmp(deletedPmps);
		checkDeletedNominalFm(deletedFms);
		
		List<NominalPmp> newNominalPmpList = new ArrayList<>();
		
		for (NominalPmp nominalPmp : model.getPmpList()) {		
			modelValidator.validateNominalPmp(nominalPmp);
			
			boolean isNewPmp = nominalPmp.getId() == null;
			
			if (isNewPmp) {	
				nominalPmp = NominalPmp.buildNominalPmp(nominalPmp, model);
				
				NominalPmp previousPmp = modelById.getPmpByName(nominalPmp.getName());
				
				if (previousPmp != null && !nominalPmp.getId().equals(previousPmp.getId())) { throw new ModelException(ModelMessage.DUPLICATE_PMP); }
				
				newNominalPmpList.add(nominalPmp);
			}else {
				NominalPmp previousPmp = modelById.getPmpById(nominalPmp.getId());
				previousPmp.setName(nominalPmp.getName());
				previousPmp.setX(nominalPmp.getX());
				previousPmp.setY(nominalPmp.getY());
				previousPmp.setZ(nominalPmp.getZ());
				previousPmp.setActive(nominalPmp.isActive());
				
				redoNominalAxisCoordinate(previousPmp.getAxisCoordinateList(), nominalPmp.getAxisCoordinateList(), previousPmp);
				
				newNominalPmpList.add(previousPmp);
			}
		}
		
		modelById.getPmpList().clear();
		modelById.getPmpList().addAll(newNominalPmpList);
		
		List<NominalFm> newNominalFmList = new ArrayList<>();
		
		for (NominalFm nominalFm: model.getFmList()) {
			modelValidator.validateNominalFm(nominalFm);
			
			boolean isNewFm = nominalFm.getId() == null;
			
			if (isNewFm) {
				NominalFm newNominalFm = NominalFm.buildNominalFm(nominalFm, model);
				
				NominalFm previousFm = modelById.getFmByName(newNominalFm.getName());
				
				if (previousFm != null && !nominalFm.getId().equals(previousFm.getId())) { throw new ModelException(ModelMessage.DUPLICATE_FM); }

				redoNominalPmp(newNominalFm.getPmpList(), nominalFm.getPmpList(), newNominalFm, modelById);
				redoFmImpactList(newNominalFm.getFmImpactList(), nominalFm.getFmImpactList(), newNominalFm);
				
				newNominalFmList.add(newNominalFm);
			}else {
				NominalFm previousFm = modelById.getFmById(nominalFm.getId());
				
				previousFm.setName(nominalFm.getName());
				previousFm.setAxis(nominalFm.getAxis());
				previousFm.setCatalogType(nominalFm.getCatalogType());
				previousFm.setDefaultValue(nominalFm.getDefaultValue());
				previousFm.setHigherTolerance(nominalFm.getHigherTolerance());
				previousFm.setLowerTolerance(nominalFm.getLowerTolerance());
				previousFm.setLevel(nominalFm.getLevel());
				previousFm.setActive(nominalFm.isActive());
				
				redoNominalPmp(previousFm.getPmpList(), nominalFm.getPmpList(), previousFm, modelById);
				redoFmImpactList(previousFm.getFmImpactList(), nominalFm.getFmImpactList(), previousFm);
				
				newNominalFmList.add(previousFm);
			}
						
		}
		
		modelById.getFmList().clear();
		modelById.getFmList().addAll(newNominalFmList);
		
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
	
	@Override
	public Set<Model> getAllModelsByUnitId(Long unitId) {
		return modelRepository.findAllPartNumbersByUnitId(unitId);
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
			fmForm.setPmpList(pointsFound);
			
			if (fmImpactHelperFound != null) {
				fmForm.setFmImpactList(parserHandleService.buildFmImpactFormBasedOnHelper(fmImpactHelperFound));
			}
			
			setFmForm.add(fmForm);
		}
		
		return setFmForm;
	}
		
	private void redoNominalAxisCoordinate(List<NominalAxisCoordinate> currentList, List<NominalAxisCoordinate> newList, NominalPmp nominalPmp) throws ModelException, EntityValidatorException {						
		for (NominalAxisCoordinate nominalAxisCoordinate : newList) {
			modelValidator.validateNominalAxisCoordinate(nominalAxisCoordinate);
			
			nominalAxisCoordinate.setNominalPmp(nominalPmp);
		}
		
		boolean hasDuplicate = newList.stream().map(axisCoordinate -> axisCoordinate.getName()).distinct().count() != newList.size();
		
		if (hasDuplicate) { throw new ModelException(ModelMessage.DUPLICATE_AXIS_COORDINATE); }
		
		currentList.clear();
		currentList.addAll(newList);
	}
	
	private void redoNominalPmp(List<NominalPmp> currentList, List<NominalPmp> newList, NominalFm previousFm, Model model) throws EntityValidatorException, ModelException {
		if (newList == null || newList.isEmpty()) { throw new ModelException(ModelMessage.NOMINAL_FM_NOMINAL_POINTS); }
		
		currentList.clear();
		
		for (NominalPmp nominalPmp : newList) {
			NominalPmp previousPmp = model.getPmpByName(nominalPmp.getName());
			
			if (previousPmp == null) { throw new ModelException(ModelMessage.PMP_NOT_FOUND); }
			
			currentList.add(previousPmp);
		}
		
		boolean hasDuplicate = newList.stream().map(nominalPmp -> nominalPmp.getName()).distinct().count() != newList.size();
		
		if (hasDuplicate) { throw new ModelException(ModelMessage.DUPLICATE_PMP_FM); }		
	}
	
	private void redoFmImpactList(List<FmImpact> currentList, List<FmImpact> newList, NominalFm nominalFm) throws EntityValidatorException {
		currentList.clear();
		
		for (FmImpact fmImpact : newList) {
			modelValidator.validateFmImpact(fmImpact);
		
			fmImpact.setNominalFm(nominalFm);
			currentList.add(fmImpact);
		}
	}
	
	private List<NominalPmp> getDeletedNominalPmp(List<NominalPmp> previousNominalPmpList, List<NominalPmp> newNominalPmpList) {
		List<NominalPmp> deletedNominalPmpList = new ArrayList<>();
		
		for (NominalPmp previousPmp : previousNominalPmpList) {
			if (newNominalPmpList.stream().anyMatch(nominalPmp -> nominalPmp.getName().equals(previousPmp.getName()))) { continue; }
			
			deletedNominalPmpList.add(previousPmp);
		}
		
		return deletedNominalPmpList;
	}
	
	private List<NominalFm> getDeletedNominalFm(List<NominalFm> previousNominalFmList, List<NominalFm> newNominalFmList) {
		List<NominalFm> deletedNominalFmList = new ArrayList<>();
		
		for (NominalFm previousFm : previousNominalFmList) {
			if (newNominalFmList.stream().anyMatch(nominalFm -> nominalFm.getName().equals(previousFm.getName()))) { continue; }
			
			deletedNominalFmList.add(previousFm);
		}
		
		return deletedNominalFmList;
	}
	
	private void checkDeletedNominalPmp(List<NominalPmp> nominalPmpList) throws ModelException {
		for (NominalPmp nominalPmp : nominalPmpList) {
			if (!nominalPmp.hasMeasurementPmps()) { continue; }
			
			throw new ModelException(ModelMessage.NOMINAL_PMP_HAS_ASSOCIATION);
		}
	}
	
	private void checkDeletedNominalFm(List<NominalFm> nominalFmList) throws ModelException {
		for (NominalFm NominalFm : nominalFmList) {
			if (!NominalFm.hasMeasurementFms()) { continue; }
			
			throw new ModelException(ModelMessage.NOMINAL_FM_HAS_ASSOCIATION);
		}
	}
		
}
