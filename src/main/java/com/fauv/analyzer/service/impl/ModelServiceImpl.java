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
import com.fauv.analyzer.entity.NominalFm;
import com.fauv.analyzer.entity.NominalPmp;
import com.fauv.analyzer.entity.dto.PmpDTO;
import com.fauv.analyzer.entity.form.FmForm;
import com.fauv.analyzer.entity.form.FmImpactForm;
import com.fauv.analyzer.entity.form.ModelForm;
import com.fauv.analyzer.entity.form.ModelPreview;
import com.fauv.analyzer.entity.form.PmpForm;
import com.fauv.analyzer.entity.helper.FmHelper;
import com.fauv.analyzer.entity.helper.PmpHelper;
import com.fauv.analyzer.entity.helper.SampleHelper;
import com.fauv.analyzer.exception.EntityValidatorException;
import com.fauv.analyzer.exception.ModelException;
import com.fauv.analyzer.message.ModelMessage;
import com.fauv.analyzer.repository.ModelRepository;
import com.fauv.analyzer.service.CarService;
import com.fauv.analyzer.service.ModelService;
import com.fauv.analyzer.service.ParserHandleService;
import com.fauv.analyzer.service.http.ParserHttp;

@Service
public class ModelServiceImpl implements ModelService {

	@Autowired
	private ParserHttp parserHttp;
	
	@Autowired
	private ParserHandleService parserHandleService;
	
	@Autowired
	private ModelRepository modelRepository;
	
	@Autowired
	private CarService carService;
	
	@Override
	public ModelPreview preview(MultipartFile dmoFile, MultipartFile csvFile) {
		SampleHelper sample = parserHttp.readDmoFileAndBuildASample(dmoFile);
				
		return createModelPreviewUsignSampleHelperAsReference(sample);
	}

	@Override
	public Model create(ModelForm form) throws EntityValidatorException {
		
		Car car = carService.getById(form.getCar().getId());
		
		Model model = new Model();
		model.setPartNumber(form.getPartNumber());
		model.setStepDescription(form.getStepDescription());
		model.setCar(car);
		
		for (PmpForm pmpForm : form.getPmpList()) {
			NominalPmp nominalPmp = NominalPmp.buildNominalPmp(pmpForm);
			nominalPmp.setModel(model);
			
			model.getPmpList().add(nominalPmp);
		}
		
		for (FmForm fmForm: form.getFmList()) {
			NominalFm nominal = NominalFm.buildNominalFm(fmForm);
			
			for (PmpDTO pmpDTO : fmForm.getPointsUsingToMap()) {
				NominalPmp nominalPmp = model.getPmpList().stream()
						.filter(pmp -> pmp.getName().equals(pmpDTO.getName())).findFirst().orElse(null);
				
				if (nominalPmp == null) { continue; }
				
				nominal.getPointsUsingToMap().add(nominalPmp);
			}
			
			for (FmImpactForm fmImpactForm : fmForm.getFmImpactList()) {
				FmImpact fmImpact = FmImpact.buildFmImpact(fmImpactForm);
				fmImpact.setNominalFm(nominal);
				
				nominal.getFmImpactList().add(fmImpact);
			}
			
			nominal.setModel(model);
			model.getFmList().add(nominal);
		}
				
		return modelRepository.save(model);
	}

	@Override
	public Model edit(Model model) throws EntityValidatorException, ModelException {
	
		Model modelById = getByIdValidateIt(model.getId());
		
		modelById.setPartNumber(model.getPartNumber());
		modelById.setStepDescription(model.getStepDescription());
		modelById.setCar(model.getCar());
		
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

	private ModelPreview createModelPreviewUsignSampleHelperAsReference(SampleHelper sampleHelper) {
		ModelPreview modelForm = new ModelPreview();
		
		List<PmpDTO> pmpDTOList = parserHandleService.buildPmpDTOBasedOnHelper(sampleHelper.getPmpList());
		
		modelForm.setPartNumber(sampleHelper.getHeader().getPartNumber());
		modelForm.setStepDescription(null);
		modelForm.setCar(null);
		modelForm.setPmpList(buildPmpFormSet(sampleHelper.getPmpList()));
		modelForm.setFmList(buildFmFormSet(sampleHelper.getFmList(), pmpDTOList));
		
		return modelForm;
	}
	
	private Set<PmpForm> buildPmpFormSet(List<PmpHelper> helperList) {
		return parserHandleService.buildPmpFormBasedOnHelper(helperList).stream().collect(Collectors.toSet());
	}
	
	private Set<FmForm> buildFmFormSet(List<FmHelper> helperList, List<PmpDTO> pmpDTOList) {
		Set<FmForm> setFmForm = new HashSet<>();
		
		for (FmHelper fmHelper : helperList) {
			List<String> pointsNameToBeFound = fmHelper.getNominalCoordinates().stream()
					.map(nominalCoordinate -> nominalCoordinate.getName()).collect(Collectors.toList());			
			
			List<PmpDTO> pointsFound = pmpDTOList.stream()
					.filter(pmpDTO -> pointsNameToBeFound.contains(pmpDTO.getName())).collect(Collectors.toList());			
			
			FmForm fmForm = parserHandleService.buildFmFormBasedOnHelper(fmHelper);
			fmForm.setPointsUsingToMap(pointsFound);

			setFmForm.add(fmForm);
		}
		
		return setFmForm;
	}
	
}
