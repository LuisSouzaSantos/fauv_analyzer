package com.fauv.analyzer.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fauv.analyzer.entity.dto.NominalAxisCoordinateDTO;
import com.fauv.analyzer.entity.dto.PmpDTO;
import com.fauv.analyzer.entity.form.FmForm;
import com.fauv.analyzer.entity.form.FmImpactForm;
import com.fauv.analyzer.entity.form.NominalAxisCoordinateForm;
import com.fauv.analyzer.entity.form.PmpForm;
import com.fauv.analyzer.entity.helper.FmHelper;
import com.fauv.analyzer.entity.helper.FmImpactHelper;
import com.fauv.analyzer.entity.helper.NominalAxisCoordinateHelper;
import com.fauv.analyzer.entity.helper.PmpHelper;
import com.fauv.analyzer.service.ParserHandleService;

@Service
public class ParserHandleImpl implements ParserHandleService {

	@Override
	public FmForm buildFmFormBasedOnHelper(FmHelper helper) {
		FmForm form = new FmForm();
		
		form.setName(helper.getName());
		form.setAxis(helper.getNominalAxisCoodinates().getAxisType());
		form.setDefaultValue(helper.getNominalAxisCoodinates().getDefaultValue());
		form.setHigherTolerance(helper.getNominalAxisCoodinates().getSuperiorTolerance());
		form.setLowerTolerance(helper.getNominalAxisCoodinates().getLowerTolerance());

		return form;
	}

	@Override
	public PmpForm buildPmpFormBasedOnHelper(PmpHelper helper) {
		PmpForm pmpForm = new PmpForm();
		pmpForm.setActive(true);
		pmpForm.setName(helper.getName());
		pmpForm.setX(helper.getNominalCoordinate().getValues().get(0).getValue());
		pmpForm.setY(helper.getNominalCoordinate().getValues().get(1).getValue());
		pmpForm.setZ(helper.getNominalCoordinate().getValues().get(2).getValue());
		pmpForm.setAxisCoordinateList(buildNominalAxisCoordinateFormBasedOnHelper(helper.getNominalAxisCoordinates()));
		
		return pmpForm;
	}

	@Override
	public NominalAxisCoordinateForm buildNominalAxisCoordinateFormBasedOnHelper(NominalAxisCoordinateHelper helper) {
		NominalAxisCoordinateForm nominalAxisCoordinateForm = new NominalAxisCoordinateForm();
		
		nominalAxisCoordinateForm.setName(helper.getName());
		nominalAxisCoordinateForm.setAxis(helper.getAxisType());
		nominalAxisCoordinateForm.setHigherTolerance(helper.getSuperiorTolerance());
		nominalAxisCoordinateForm.setLowerTolerance(helper.getLowerTolerance());
		
		return nominalAxisCoordinateForm;
	}
	
	@Override
	public List<NominalAxisCoordinateForm> buildNominalAxisCoordinateFormBasedOnHelper(
			List<NominalAxisCoordinateHelper> helperList) {
		return helperList.stream().map(helper -> buildNominalAxisCoordinateFormBasedOnHelper(helper)).collect(Collectors.toList());
	}

	@Override
	public List<FmForm> buildFmFormBasedOnHelper(List<FmHelper> helperList) {
		return helperList.stream().map(form -> buildFmFormBasedOnHelper(form)).collect(Collectors.toList());
	}

	@Override
	public List<PmpForm> buildPmpFormBasedOnHelper(List<PmpHelper> helperList) {
		return helperList.stream().map(form -> buildPmpFormBasedOnHelper(form)).collect(Collectors.toList());
	}

	@Override
	public PmpDTO buildPmpDTOBasedOnHelper(PmpHelper helper) {
		PmpDTO pmpDTO = new PmpDTO();
		
		pmpDTO.setName(helper.getName());
		pmpDTO.setX(helper.getNominalCoordinate().getValues().get(0).getValue());
		pmpDTO.setY(helper.getNominalCoordinate().getValues().get(1).getValue());
		pmpDTO.setZ(helper.getNominalCoordinate().getValues().get(2).getValue());
		pmpDTO.setActive(true);
		pmpDTO.setAxisCoordinateList(buildNominalAxisCoordinateDTOBasedOnHelper(helper.getNominalAxisCoordinates()));
		
		return pmpDTO;
	}

	@Override
	public List<PmpDTO> buildPmpDTOBasedOnHelper(List<PmpHelper> helperList) {
		return helperList.stream().map(helper -> buildPmpDTOBasedOnHelper(helper)).collect(Collectors.toList());
	}

	// ID is not include in this method
	@Override
	public NominalAxisCoordinateDTO buildNominalAxisCoordinateDTOBasedOnHelper(NominalAxisCoordinateHelper helper) {
		NominalAxisCoordinateDTO nominalAxisCoordinateDTO = new NominalAxisCoordinateDTO();
		
		nominalAxisCoordinateDTO.setName(helper.getName());
		nominalAxisCoordinateDTO.setAxis(helper.getAxisType());
		nominalAxisCoordinateDTO.setHigherTolerance(helper.getSuperiorTolerance());
		nominalAxisCoordinateDTO.setLowerTolerance(helper.getLowerTolerance());
		
		return nominalAxisCoordinateDTO;
	}

	// ID is not include in this method
	@Override
	public List<NominalAxisCoordinateDTO> buildNominalAxisCoordinateDTOBasedOnHelper(
			List<NominalAxisCoordinateHelper> helperList) {
		return helperList.stream().map(helper -> buildNominalAxisCoordinateDTOBasedOnHelper(helper)).collect(Collectors.toList());
	}
	
	@Override
	public List<FmImpactForm> buildFmImpactFormBasedOnHelper(FmImpactHelper fmImpactHelper) {
		return fmImpactHelper.getInformationList().stream()
				.map(fmImpact -> buildFmImpactFormBasedOnHelper(fmImpact)).collect(Collectors.toList());
	}

	@Override
	public FmImpactForm buildFmImpactFormBasedOnHelper(String info) {
		FmImpactForm fmImpactForm = new FmImpactForm();
		fmImpactForm.setInfo(info);
	
		return fmImpactForm;
	}

}
