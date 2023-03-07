package com.fauv.analyzer.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fauv.analyzer.entity.Model;
import com.fauv.analyzer.entity.NominalFm;
import com.fauv.analyzer.entity.NominalPmp;
import com.fauv.analyzer.entity.dto.FmDTO;
import com.fauv.analyzer.entity.dto.ModelDTO;
import com.fauv.analyzer.entity.dto.PmpDTO;
import com.fauv.analyzer.entity.form.FmForm;
import com.fauv.analyzer.entity.form.PmpForm;
import com.fauv.analyzer.service.ModelHelperService;

@Service
public class ModelHelperServiceImpl implements ModelHelperService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public ModelDTO toModelDTO(Model model) {
		if (model == null) { return null; }
		
		return modelMapper.map(model, ModelDTO.class);
	}

	@Override
	public List<ModelDTO> toModelDTO(List<Model> list) {
		if (list == null) { return null; }
		
		return list.stream().map(item -> toModelDTO(item)).collect(Collectors.toList());
	}

	@Override
	public Model toModel(ModelDTO modelDto) {
		if (modelDto == null) { return null; }
		
		return modelMapper.map(modelDto, Model.class);
	}

	@Override
	public NominalFm toNominalFm(FmDTO fmDTO) {
		if (fmDTO == null) { return null; }
		
		return modelMapper.map(fmDTO, NominalFm.class);
	}

	@Override
	public NominalFm toNominalFm(FmForm fmForm) {
		if (fmForm == null) { return null; }
		
		return modelMapper.map(fmForm, NominalFm.class);
	}

	@Override
	public List<NominalFm> toNominalFm(FmDTO[] list) {
		if (list == null) { return null; }
		
		return Arrays.asList(list).stream().map(item -> toNominalFm(item)).collect(Collectors.toList());
	}

	@Override
	public List<NominalFm> toNominalFm(FmForm[] list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NominalPmp toNominalPmp(PmpDTO pmpDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NominalPmp toNominalPmp(PmpForm pmpForm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominalPmp> toNominalPmp(PmpDTO[] list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NominalPmp> toNominalPmp(PmpForm[] pmpDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FmDTO toNominalFmDTO(NominalFm nominalFm) {
		// TODO Auto-generated method stub
		return null;
	}

}
