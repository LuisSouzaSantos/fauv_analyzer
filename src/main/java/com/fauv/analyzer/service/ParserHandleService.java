package com.fauv.analyzer.service;

import java.util.List;

import com.fauv.analyzer.entity.dto.NominalAxisCoordinateDTO;
import com.fauv.analyzer.entity.dto.PmpDTO;
import com.fauv.analyzer.entity.form.FmForm;
import com.fauv.analyzer.entity.form.NominalAxisCoordinateForm;
import com.fauv.analyzer.entity.form.PmpForm;
import com.fauv.analyzer.entity.helper.FmHelper;
import com.fauv.analyzer.entity.helper.NominalAxisCoordinateHelper;
import com.fauv.analyzer.entity.helper.PmpHelper;

public interface ParserHandleService {

	public FmForm buildFmFormBasedOnHelper(FmHelper helper);
	
	public PmpForm buildPmpFormBasedOnHelper(PmpHelper helper);
	
	public PmpDTO buildPmpDTOBasedOnHelper(PmpHelper helper);
	
	public List<PmpForm> buildPmpFormBasedOnHelper(List<PmpHelper> helperList);
	
	public List<FmForm> buildFmFormBasedOnHelper(List<FmHelper> helperList);
	
	public List<PmpDTO> buildPmpDTOBasedOnHelper(List<PmpHelper> helperList);
	
	public NominalAxisCoordinateForm buildNominalAxisCoordinateFormBasedOnHelper(NominalAxisCoordinateHelper helper);
	
	public List<NominalAxisCoordinateForm> buildNominalAxisCoordinateFormBasedOnHelper(List<NominalAxisCoordinateHelper> helperList);

	public NominalAxisCoordinateDTO buildNominalAxisCoordinateDTOBasedOnHelper(NominalAxisCoordinateHelper helper);
	
	public List<NominalAxisCoordinateDTO> buildNominalAxisCoordinateDTOBasedOnHelper(List<NominalAxisCoordinateHelper> helperList);
	
}
