package com.fauv.analyzer.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fauv.analyzer.entity.Equipment;
import com.fauv.analyzer.entity.MeasurementAxisCoordinate;
import com.fauv.analyzer.entity.MeasurementFm;
import com.fauv.analyzer.entity.MeasurementPmp;
import com.fauv.analyzer.entity.Model;
import com.fauv.analyzer.entity.NominalAxisCoordinate;
import com.fauv.analyzer.entity.NominalPmp;
import com.fauv.analyzer.entity.Sample;
import com.fauv.analyzer.entity.Unit;
import com.fauv.analyzer.entity.dto.MeasurementAxisCoordinateDTO;
import com.fauv.analyzer.entity.dto.MeasurementFmDTO;
import com.fauv.analyzer.entity.dto.MeasurementPmpDTO;
import com.fauv.analyzer.entity.dto.SampleDTO;
import com.fauv.analyzer.entity.helper.CoordinateValueHelper;
import com.fauv.analyzer.entity.helper.FmHelper;
import com.fauv.analyzer.entity.helper.MeasurementAxisCoordinateHelper;
import com.fauv.analyzer.entity.helper.PmpHelper;
import com.fauv.analyzer.entity.helper.SampleHelper;
import com.fauv.analyzer.enums.AxisType;
import com.fauv.analyzer.enums.ToleranceType;
import com.fauv.analyzer.exception.UnitException;
import com.fauv.analyzer.repository.SampleRepository;
import com.fauv.analyzer.service.EquipmentService;
import com.fauv.analyzer.service.ModelService;
import com.fauv.analyzer.service.SampleService;
import com.fauv.analyzer.service.UnitService;
import com.fauv.analyzer.service.http.ParserHttp;

@Service
public class SampleServiceImpl implements SampleService {

	@Autowired
	private SampleRepository sampleRepository;
	
	@Autowired
	private EquipmentService equipmentService;
	
	@Autowired
	private UnitService unitService;
	
	@Autowired
	private ModelService modelService;
	
	@Autowired
	private ParserHttp parserHttp;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Sample save(MultipartFile dmoFile, Long unitId) throws UnitException {
		SampleHelper sampleHelper = parserHttp.readDmoFileAndBuildASample(dmoFile);
		
		Unit unit = unitService.getByIdValidateIt(unitId);
		Equipment equipment = equipmentService.getByNameAndUnit(sampleHelper.getHeader().getEquipmentName(), unit);
		Model model = modelService.getByPartNumberAndUnit(sampleHelper.getHeader().getPartNumber(), unit);
	
		Sample sample = new Sample();
		sample.setFileName("not_include");
		sample.setModel(model);
		sample.setPin(sampleHelper.getHeader().getSampleId());
		sample.setEquipment(equipment);
		sample.setUploadedUser(sampleHelper.getHeader().getInspectorName());
		sample.setScanInitDate(sampleHelper.getHeader().getStartDate(), sampleHelper.getHeader().getStartTime());
		sample.setScanEndDate(sampleHelper.getHeader().getEndDate(), sampleHelper.getHeader().getEndTime());
		sample.setUploadedDate(LocalDateTime.now());
		
		sample.getMeasurementPmpList().addAll(buildMeasurementPmp(sampleHelper.getPmpList(), sample, model));
		sample.getMeasurementFmList().addAll(buildMeasurementFm(sampleHelper.getFmList(),sample.getMeasurementPmpList(), sample, model));
		
		
		return sample;
	}

	@Override
	public List<Sample> getAll() {
		return sampleRepository.findAll();
	}

	@Override
	public Sample getById(Long id) {
		return sampleRepository.findById(id).orElse(null);
	}
	
	@Override
	public List<SampleDTO> toSampleDTO(List<Sample> list) {
		if (list == null) { return new ArrayList<>(); }
		
		return list.stream().map(item -> toSampleDTO(item)).collect(Collectors.toList());
	}
	
	@Override
	public SampleDTO toSampleDTO(Sample sample) {
		if (sample == null) { return null; }
		
		SampleDTO sampleDTO = modelMapper.map(sample, SampleDTO.class);
		sampleDTO.getMeasurementPmpList().addAll(buildMeasurementPmpDTO(sample.getMeasurementPmpList()));
		sampleDTO.getMeasurementFmList().addAll(buildMeasurementFmDTO(sample.getMeasurementFmList()));
		
		return sampleDTO;
	}
	
	private Set<MeasurementPmpDTO> buildMeasurementPmpDTO(Set<MeasurementPmp> measurementPmpList) {
		Set<MeasurementPmpDTO> measurementPmpDTOList = new HashSet<MeasurementPmpDTO>();
		
		for (MeasurementPmp measurementPmp : measurementPmpList) {
			MeasurementPmpDTO measurementPmpDTO = new MeasurementPmpDTO();
			measurementPmpDTO.setX(measurementPmp.getX().doubleValue());
			measurementPmpDTO.setY(measurementPmp.getY().doubleValue());
			measurementPmpDTO.setZ(measurementPmp.getZ().doubleValue());
			measurementPmpDTO.setDefaultX(measurementPmp.getNominalPmp().getX().doubleValue());
			measurementPmpDTO.setDefaultY(measurementPmp.getNominalPmp().getY().doubleValue());
			measurementPmpDTO.setDefaultZ(measurementPmp.getNominalPmp().getZ().doubleValue());
			measurementPmpDTO.setName(measurementPmpDTO.getName());
			measurementPmpDTO.setAxis(measurementPmp.getNominalPmp().getAxis());
			measurementPmpDTO.getMeasurementAxisCoordinateList().addAll(buildMeasurementAxisCoordinateDTO(measurementPmp.getMeasurementAxisCoordinateList()));
			
			measurementPmpDTOList.add(measurementPmpDTO);
		}
		
		return measurementPmpDTOList;
	}
	
	private Set<MeasurementAxisCoordinateDTO> buildMeasurementAxisCoordinateDTO(Set<MeasurementAxisCoordinate> measurementAxisCoordinateList) {
		Set<MeasurementAxisCoordinateDTO> measurementAxisCoordinateDTOList = new HashSet<>();
		
		for (MeasurementAxisCoordinate measurementAxisCoordinate : measurementAxisCoordinateList) {
						
			MeasurementAxisCoordinateDTO measurementAxisCoordinateDTO = new MeasurementAxisCoordinateDTO();
			measurementAxisCoordinateDTO.setName(measurementAxisCoordinate.getNominalAxisCoordinate().getName());
			measurementAxisCoordinateDTO.setToleranceType(measurementAxisCoordinate.getToleranceType());
			measurementAxisCoordinateDTO.setValue(measurementAxisCoordinate.getValue());
			measurementAxisCoordinateDTO.setAxis(measurementAxisCoordinate.getNominalAxisCoordinate().getAxis());
			measurementAxisCoordinateDTO.setHigherTolerance(measurementAxisCoordinate.getNominalAxisCoordinate().getHigherTolerance().doubleValue());
			measurementAxisCoordinateDTO.setLowerTolerance(measurementAxisCoordinate.getNominalAxisCoordinate().getLowerTolerance().doubleValue());
			
			measurementAxisCoordinateDTOList.add(measurementAxisCoordinateDTO);
		}
		
		return measurementAxisCoordinateDTOList;
	}
	
	private Set<MeasurementFmDTO> buildMeasurementFmDTO(Set<MeasurementFm> measurementFmList) {
		Set<MeasurementFmDTO> measurementFmDTOList = new HashSet<>();
		
		for (MeasurementFm measurementFm : measurementFmList) {
			MeasurementFmDTO measurementFmDTO = new MeasurementFmDTO();
			measurementFmDTO.setName(measurementFm.getNominalFm().getName());
			measurementFmDTO.setCatalogType(measurementFm.getNominalFm().getCatalogType());
			measurementFmDTO.setHigherTolerance(measurementFm.getNominalFm().getHigherTolerance().doubleValue());
			measurementFmDTO.setLowerTolerance(measurementFm.getNominalFm().getLowerTolerance().doubleValue());
			measurementFmDTO.setToleranceType(measurementFm.getToleranceType());
			measurementFmDTO.setValue(measurementFm.getValue());
			measurementFmDTO.setAxis(measurementFm.getNominalFm().getAxis());
			measurementFmDTO.setDefaultValue(measurementFm.getNominalFm().getDefaultValue().doubleValue());
			measurementFmDTO.setLevel(measurementFm.getNominalFm().getLevel());
			measurementFmDTO.getMeasurementPmpList().addAll(measurementFm.getMeasurementPmpNameList());
			
			measurementFmDTOList.add(measurementFmDTO);
		}
		
		
		return measurementFmDTOList;
	}
	
	private Set<MeasurementFm> buildMeasurementFm(List<FmHelper> fmListHelper, Set<MeasurementPmp> measurementPmpList, Sample sample, Model model) {
		Set<MeasurementFm> measurementFmList = new HashSet<>();
		
		for (FmHelper fmHelper : fmListHelper) {
			MeasurementFm measurementFm = new MeasurementFm();
			measurementFm.setSample(sample);
			measurementFm.setToleranceType(ToleranceType.valueOf(fmHelper.getMeasurementAxisCoordinates().getType().name()));
			measurementFm.setValue(new BigDecimal(fmHelper.getMeasurementAxisCoordinates().getCalculated()));
			measurementFm.setNominalFm(model.getFmByName(fmHelper.getName()));
			
			for (String pmpName : fmHelper.getPmpNameList()) {
				MeasurementPmp measurementPmpFound = measurementPmpList.stream()
						.filter(measurementPmp -> measurementPmp.getNominalPmp().getName().equals(pmpName)).findFirst().orElse(null);
				
				if (measurementPmpFound == null) { continue; }
				
				measurementFm.getMeasurementPmpList().add(measurementPmpFound);
			}
			
			measurementFmList.add(measurementFm);
		}
		
		return measurementFmList;
	}
	
	private Set<MeasurementPmp> buildMeasurementPmp(List<PmpHelper> pmpListHelper, Sample sample, Model model) {
		Set<MeasurementPmp> measurementPmpList = new HashSet<>();
		
		for (PmpHelper pmpHelper : pmpListHelper) {
			MeasurementPmp measurementPmp = new MeasurementPmp();
			
			for (CoordinateValueHelper coordinateValueHelper : pmpHelper.getMeasurementCoordinate().getValues()) {
				if (coordinateValueHelper.getAxisType().equals(AxisType.X)) {
					measurementPmp.setX(new BigDecimal(coordinateValueHelper.getValue()));
				}
				
				if (coordinateValueHelper.getAxisType().equals(AxisType.Y)) {
					measurementPmp.setY(new BigDecimal(coordinateValueHelper.getValue()));
				}
				
				if (coordinateValueHelper.getAxisType().equals(AxisType.Z)) {
					measurementPmp.setZ(new BigDecimal(coordinateValueHelper.getValue()));
				}
			}
			
			measurementPmp.setNominalPmp(model.getPmpByName(pmpHelper.getName()));
			measurementPmp.setSample(sample);
			measurementPmp.setMeasurementAxisCoordinateList(
					buildMeasurementAxisCoordiante(pmpHelper.getMeasurementAxisCoordinates(), measurementPmp));

			measurementPmpList.add(measurementPmp);
		}
		
		return measurementPmpList;
	}
	
	private Set<MeasurementAxisCoordinate> buildMeasurementAxisCoordiante(
			List<MeasurementAxisCoordinateHelper> measurementAxisCoordinateListHelper, MeasurementPmp measurementPmp) {
		Set<MeasurementAxisCoordinate> measurementAxisCoordinateList = new HashSet<>();
			
		NominalPmp nominalPmp = measurementPmp.getNominalPmp();
		
		for (MeasurementAxisCoordinateHelper measurementAxisCoordinateHelper : measurementAxisCoordinateListHelper) {
			
			NominalAxisCoordinate nominalAxisCoordinate = nominalPmp.getAxisCoordinateByName(measurementAxisCoordinateHelper.getName());
			
			MeasurementAxisCoordinate measurementAxisCoordinate = new MeasurementAxisCoordinate();
			measurementAxisCoordinate.setMeasurementPmp(measurementPmp);
			measurementAxisCoordinate.setToleranceType(ToleranceType.valueOf(measurementAxisCoordinateHelper.getType().name()));
			measurementAxisCoordinate.setValue(new BigDecimal(measurementAxisCoordinateHelper.getCalculated()));
			measurementAxisCoordinate.setNominalAxisCoordinate(nominalAxisCoordinate);
			
			measurementAxisCoordinateList.add(measurementAxisCoordinate);
		}
		
		return measurementAxisCoordinateList;
	}



}
