package com.fauv.analyzer.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fauv.analyzer.entity.Equipment;
import com.fauv.analyzer.entity.MeasurementAxisCoordinate;
import com.fauv.analyzer.entity.MeasurementFm;
import com.fauv.analyzer.entity.MeasurementPmp;
import com.fauv.analyzer.entity.Model;
import com.fauv.analyzer.entity.NominalAxisCoordinate;
import com.fauv.analyzer.entity.NominalFm;
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
import com.fauv.analyzer.enums.StatusType;
import com.fauv.analyzer.enums.ToleranceType;
import com.fauv.analyzer.exception.EquipmentException;
import com.fauv.analyzer.exception.ModelException;
import com.fauv.analyzer.exception.SampleException;
import com.fauv.analyzer.exception.UnitException;
import com.fauv.analyzer.message.SampleMessage;
import com.fauv.analyzer.repository.SampleRepository;
import com.fauv.analyzer.service.CalcService;
import com.fauv.analyzer.service.EquipmentService;
import com.fauv.analyzer.service.ModelHelperService;
import com.fauv.analyzer.service.ModelService;
import com.fauv.analyzer.service.SampleService;
import com.fauv.analyzer.service.UnitService;
import com.fauv.analyzer.service.http.ParserHttp;

@Service
public class SampleServiceImpl implements SampleService {

	//private static Logger logger = LoggerFactory.getLogger(SampleServiceImpl.class);
	
	@Autowired
	private SampleRepository sampleRepository;
	
	@Autowired
	private EquipmentService equipmentService;
	
	@Autowired
	private UnitService unitService;
	
	@Autowired
	private ModelService modelService;
	
	@Autowired
	private ModelHelperService modelHelperService;
	
	@Autowired
	private CalcService calcService;
	
	@Autowired
	private ParserHttp parserHttp;
	
	@Autowired
	private EntityManager entityManager;
	
	@Override
	public Sample save(MultipartFile dmoFile, Long unitId) throws UnitException, EquipmentException, ModelException, SampleException {		
		SampleHelper sampleHelper = parserHttp.readDmoFileAndBuildASample(dmoFile);
		
		if (sampleHelper == null || !sampleHelper.isValid()) { throw new SampleException(SampleMessage.NOT_RECOGNIZED); }
		
		Unit unit = unitService.getByIdValidateIt(unitId);
		Equipment equipment = equipmentService.getByNameAndUnitValidateIt(sampleHelper.getHeader().getEquipmentName(), unit);
		Model model = modelService.getByPartNumberAndUnitValidateIt(sampleHelper.getHeader().getPartNumber(), unit);
		
		Sample duplicateSample = getByPinAndModel(sampleHelper.getHeader().getSampleId(), model);
		
		if (duplicateSample != null) { throw new SampleException(SampleMessage.DUPLICATE); }
		 
		Sample sample = new Sample();
		sample.setFileName(dmoFile.getOriginalFilename());
		sample.setModel(model);
		sample.setPin(sampleHelper.getHeader().getSampleId());
		sample.setEquipment(equipment);
		sample.setUploadedUser(sampleHelper.getHeader().getInspectorName());
		sample.setScanInitDate(sampleHelper.getHeader().getStartDate(), sampleHelper.getHeader().getStartTime());
		sample.setScanEndDate(sampleHelper.getHeader().getEndDate(), sampleHelper.getHeader().getEndTime());
		sample.setUploadedDate(LocalDateTime.now());
		
		sample.getMeasurementPmpList().addAll(buildMeasurementPmp(sampleHelper.getPmpList(), sample, model));
		sample.getMeasurementFmList().addAll(buildMeasurementFm(sampleHelper.getFmList(),sample.getMeasurementPmpList(), sample, model));
		
		boolean fmsWereFound = sample.getMeasurementFmList().stream().allMatch(fm -> fm.getWasFound().equals(true));
		boolean pmpWereFound = sample.getMeasurementPmpList().stream().allMatch(pmp -> pmp.getWasFound().equals(true));
		boolean axisCoordinateWereFound = sample.getMeasurementPmpList().stream().allMatch(pmp -> {
			boolean wereFound = pmp.getMeasurementAxisCoordinateList().stream().allMatch(axisCoordinate -> axisCoordinate.getWasFound().equals(true)); 
			
			return wereFound;
		});
		
		boolean isSuccess = fmsWereFound && pmpWereFound && axisCoordinateWereFound;
		boolean isError = !fmsWereFound;
		
		StatusType status = isSuccess ? StatusType.SUCCESS : StatusType.WARNING;
		status = isError ? StatusType.ERROR : status;
		
		sample.setStatus(status);
		
		return sampleRepository.save(sample);
	}
	
	@Override
	public void delete(Long id) {
		sampleRepository.deleteById(id);		
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
	public Sample getByIdValidateIt(Long id) throws SampleException {
		Sample sample = getById(id);
		
		if (sample == null) { throw new SampleException(SampleMessage.NOT_FOUND); }
		
		return sample;
	}
	
	@Override
	public List<SampleDTO> toSampleDTO(List<Sample> list) {
		if (list == null) { return new ArrayList<>(); }
		
		return list.stream().map(item -> toSampleDTO(item)).collect(Collectors.toList());
	}
	
	@Override
	public SampleDTO toSampleDTO(Sample sample) {
		if (sample == null) { return null; }
		
		SampleDTO sampleDTO = new SampleDTO();
		
		sampleDTO.setId(sample.getId());
		sampleDTO.setFileName(sample.getFileName());
		sampleDTO.setPin(sample.getPin());
		sampleDTO.setUploadedUser(sample.getUploadedUser());
		sampleDTO.setUploadedDate(sample.getUploadedDate());
		sampleDTO.setStatus(sample.getStatus());
		sampleDTO.setScanInitDate(sample.getScanInitDate());
		sampleDTO.setScanEndDate(sample.getScanEndDate());
		sampleDTO.setModel(modelHelperService.toModelDTO(sample.getModel()));
		sampleDTO.setEquipment(equipmentService.toEquipmentDTO(sample.getEquipment()));
		
		sampleDTO.getMeasurementPmpList().addAll(buildMeasurementPmpDTO(sample.getMeasurementPmpList()));
		sampleDTO.getMeasurementFmList().addAll(buildMeasurementFmDTO(sample.getMeasurementFmList()));
		sampleDTO.setFmIndicator(calcService.calcFmIndicatorUsingDTO(sampleDTO.getMeasurementFmList()));
		sampleDTO.setPmpIndicator(calcService.calcPmpIndicatorUsingDTO(sampleDTO.getMeasurementPmpList()));
				
		return sampleDTO;
	}
	
	@Override
	public Set<SampleDTO> getByModels(Set<Model> models) {
		Set<Sample> samples = sampleRepository.findByModelIn(models);

		return samples.stream().map(sample -> toSampleDTO(sample)).collect(Collectors.toSet());
	}	
	
	@Override
	public List<MeasurementFm> getMeasurementFmBasedOnModelAndFmName(Model model, String fmName) {
		String jpql = "SELECT mf FROM Sample s INNER JOIN s.measurementFmList mf WHERE mf.nominalFm.name = :fmName AND s.model.id = :modelId ORDER BY s.scanEndDate";
		
		TypedQuery<MeasurementFm> query = entityManager.createQuery(jpql, MeasurementFm.class);
		query.setParameter("fmName", fmName);
		query.setParameter("modelId", model.getId());
		query.setMaxResults(25);
		
		return query.getResultList();
	}
		
	private Sample getByPinAndModel(String pin, Model model) {
		return sampleRepository.findByPinAndModel(pin, model);
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
			measurementPmpDTO.setName(measurementPmp.getNominalPmp().getName());
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
			measurementFmDTO.setValue(measurementFm.getValue().doubleValue());
			measurementFmDTO.setAxis(measurementFm.getNominalFm().getAxis());
			measurementFmDTO.setDefaultValue(measurementFm.getNominalFm().getDefaultValue().doubleValue());
			measurementFmDTO.setLevel(measurementFm.getNominalFm().getLevel());
			measurementFmDTO.getMeasurementPmpList().addAll(measurementFm.getMeasurementPmpNameList());
			
			measurementFmDTOList.add(measurementFmDTO);
		}
		
		return measurementFmDTOList;
	}
	
	private Set<MeasurementFm> buildMeasurementFm(List<FmHelper> fmListHelper, Set<MeasurementPmp> measurementPmpList, Sample sample, Model model) throws SampleException {
		Set<MeasurementFm> measurementFmList = new HashSet<>();
		
		for (NominalFm nominalFm : model.getFmList()) {
			FmHelper fmHelper = fmListHelper.stream().filter(fm -> fm.getName().equals(nominalFm.getName())).findFirst().orElse(null);
			
			boolean wasFound = fmHelper != null;
			
			MeasurementFm measurementFm = new MeasurementFm();
			measurementFm.setNominalFm(nominalFm);
			measurementFm.setSample(sample);
			measurementFm.setValue(new BigDecimal(0.0));
			measurementFm.setToleranceType(ToleranceType.OUTOL);
			measurementFm.setWasFound(wasFound);
			
			if (wasFound) { 
				measurementFm.setToleranceType(ToleranceType.valueOf(fmHelper.getMeasurementAxisCoordinates().getType().name()));
				measurementFm.setValue(new BigDecimal(fmHelper.getMeasurementAxisCoordinates().getCalculated()));
			}
			
		}
		
		return measurementFmList;
	}
	
	private Set<MeasurementPmp> buildMeasurementPmp(List<PmpHelper> pmpListHelper, Sample sample, Model model) throws SampleException {
		Set<MeasurementPmp> measurementPmpList = new HashSet<>();
		
		
		for (NominalPmp nominalPmp : model.getPmpList()) {
			PmpHelper pmpHelper = pmpListHelper.stream().filter(pmp -> pmp.getName().equals(nominalPmp.getName())).findFirst().orElse(null);
			
			boolean wasFound = pmpHelper!=null;
			
			MeasurementPmp measurementPmp = new MeasurementPmp();
			measurementPmp.setNominalPmp(nominalPmp);
			measurementPmp.setSample(sample);
			measurementPmp.setX(new BigDecimal(0.0));
			measurementPmp.setY(new BigDecimal(0.0));
			measurementPmp.setZ(new BigDecimal(0.0));
			measurementPmp.setWasFound(wasFound);
			
			if (wasFound) {
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
			}
			
			List<MeasurementAxisCoordinateHelper> measurementAxisCoordinateListHelper = (pmpHelper != null && pmpHelper.getMeasurementAxisCoordinates() != null)
					? pmpHelper.getMeasurementAxisCoordinates()
					: new ArrayList<>();
			
			Set<MeasurementAxisCoordinate> measurementAxisCoordinate = buildMeasurementAxisCoordiante(measurementAxisCoordinateListHelper, measurementPmp);
			
			measurementPmp.setMeasurementAxisCoordinateList(measurementAxisCoordinate);			
			measurementPmpList.add(measurementPmp);
			
		}
		
		return measurementPmpList;
	}
	
	private Set<MeasurementAxisCoordinate> buildMeasurementAxisCoordiante(
			List<MeasurementAxisCoordinateHelper> measurementAxisCoordinateListHelper, MeasurementPmp measurementPmp) throws SampleException {
		Set<MeasurementAxisCoordinate> measurementAxisCoordinateList = new HashSet<>();
			
		NominalPmp nominalPmp = measurementPmp.getNominalPmp();
		
		for (NominalAxisCoordinate nominalAxisCoordinate : nominalPmp.getAxisCoordinateList()) {
			MeasurementAxisCoordinateHelper measurementAxisCoordinateHelper = measurementAxisCoordinateListHelper.stream()
					.filter(axisCoordinate -> axisCoordinate.getName()
							.equals(nominalAxisCoordinate.getName()))
					.findAny().orElse(null);
			
			boolean wasFound = measurementAxisCoordinateHelper != null;
			
			MeasurementAxisCoordinate measurementAxisCoordinate = new MeasurementAxisCoordinate();
			measurementAxisCoordinate.setMeasurementPmp(measurementPmp);
			measurementAxisCoordinate.setNominalAxisCoordinate(nominalAxisCoordinate);
			measurementAxisCoordinate.setValue(new BigDecimal(0.0));
			measurementAxisCoordinate.setWasFound(wasFound);
			measurementAxisCoordinate.setToleranceType(ToleranceType.OUTOL);
			
			if (wasFound) {
				measurementAxisCoordinate.setToleranceType(ToleranceType.valueOf(measurementAxisCoordinateHelper.getType().name()));
				measurementAxisCoordinate.setValue(new BigDecimal(measurementAxisCoordinateHelper.getCalculated()));
			}
			
			measurementAxisCoordinateList.add(measurementAxisCoordinate);
		}
		
		return measurementAxisCoordinateList;
	}

}
