package com.fauv.analyzer.service;

import java.util.List;

import com.fauv.analyzer.entity.Model;
import com.fauv.analyzer.entity.NominalFm;
import com.fauv.analyzer.entity.NominalPmp;
import com.fauv.analyzer.entity.dto.FmDTO;
import com.fauv.analyzer.entity.dto.ModelDTO;
import com.fauv.analyzer.entity.dto.PmpDTO;
import com.fauv.analyzer.entity.form.FmForm;
import com.fauv.analyzer.entity.form.PmpForm;

public interface ModelHelperService {
	
	public ModelDTO toModelDTO(Model model);

	public List<ModelDTO> toModelDTO(List<Model> list);

	public Model toModel(ModelDTO modelDto);
	
	public NominalFm toNominalFm(FmDTO fmDTO);
	
	public NominalFm toNominalFm(FmForm fmForm);
	
	public List<NominalFm> toNominalFm(FmDTO[] list);
	
	public List<NominalFm> toNominalFm(FmForm[] list);
	
	public NominalPmp toNominalPmp(PmpDTO pmpDTO);
	
	public NominalPmp toNominalPmp(PmpForm pmpForm);
	
	public List<NominalPmp> toNominalPmp(PmpDTO[] list);

	public List<NominalPmp> toNominalPmp(PmpForm[] pmpDTO);
	
	public FmDTO toNominalFmDTO(NominalFm nominalFm);
	

}
