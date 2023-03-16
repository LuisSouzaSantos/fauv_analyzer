package com.fauv.analyzer.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fauv.analyzer.entity.FmImpact;
import com.fauv.analyzer.entity.Model;
import com.fauv.analyzer.entity.NominalAxisCoordinate;
import com.fauv.analyzer.entity.NominalFm;
import com.fauv.analyzer.entity.NominalPmp;
import com.fauv.analyzer.entity.form.ModelForm;
import com.fauv.analyzer.exception.EntityValidatorException;
import com.fauv.analyzer.exception.ModelException;
import com.fauv.analyzer.message.ModelMessage;

@Service
public class ModelValidator {

	@Autowired
	private EntityValidator<Model> modelValidator;
	
	@Autowired
	private EntityValidator<NominalPmp> nominalPmpValidator;
	
	@Autowired
	private EntityValidator<NominalAxisCoordinate> nominalAxisCoordinateValidator;
	
	@Autowired
	private EntityValidator<NominalFm> nominalFmValidator;
	
	@Autowired
	private EntityValidator<FmImpact> fmImpactValidator;
	
	@Autowired
	private EntityValidator<ModelForm> modelFormValidator;
	
	public void validateModelForm(ModelForm modelForm) throws EntityValidatorException {
		modelFormValidator.validateFields(modelForm);
	}
	
	public void validateFmImpact(FmImpact fmImpact) throws EntityValidatorException {
		fmImpactValidator.validateFields(fmImpact);
	}
	
	public void validateModel(Model model) throws EntityValidatorException {
		modelValidator.validateFields(model);
	}
	
	public void validateNominalAxisCoordinate(NominalAxisCoordinate nominalAxisCoordinate) throws EntityValidatorException {
		nominalAxisCoordinateValidator.validateFields(nominalAxisCoordinate);
	}
	
	public void validateNominalPmp(NominalPmp nominalPmp) throws EntityValidatorException, ModelException {
		nominalPmpValidator.validateFields(nominalPmp);
		
		if (nominalPmp.getAxisCoordinateList() == null || nominalPmp.getAxisCoordinateList().isEmpty()) { throw new ModelException(ModelMessage.NOMINAL_PMP_AXIS_COORDINATE_LIST); }
	}
	
	public void validateNominalFm(NominalFm nominalFm) throws EntityValidatorException, ModelException {
		nominalFmValidator.validateFields(nominalFm);
		
		if (nominalFm.getPointsUsingToMap() == null || nominalFm.getPointsUsingToMap().isEmpty()) { throw new ModelException(ModelMessage.NOMINAL_FM_NOMINAL_POINTS); }
	}
	
	
}
