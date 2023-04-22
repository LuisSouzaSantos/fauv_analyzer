package com.fauv.analyzer.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.fauv.analyzer.entity.indicators.FmIndicator;
import com.fauv.analyzer.entity.indicators.PmpIndicator;
import com.fauv.analyzer.enums.AxisType;
import com.fauv.analyzer.enums.StatusType;
import com.fauv.analyzer.enums.ToleranceType;
import com.fauv.analyzer.exception.EquipmentException;
import com.fauv.analyzer.exception.ModelException;
import com.fauv.analyzer.exception.SampleException;
import com.fauv.analyzer.exception.UnitException;
import com.fauv.analyzer.message.SampleMessage;
import com.fauv.analyzer.repository.SampleRepository;
import com.fauv.analyzer.service.EquipmentService;
import com.fauv.analyzer.service.ModelHelperService;
import com.fauv.analyzer.service.ModelService;
import com.fauv.analyzer.service.SampleService;
import com.fauv.analyzer.service.UnitService;
import com.fauv.analyzer.service.http.ParserHttp;

@Service
public class SampleServiceImpl implements SampleService {

	private static Logger logger = LoggerFactory.getLogger(SampleServiceImpl.class);
    private static final DecimalFormat FM_FORMAT_CALCULATE = new DecimalFormat("#.#");
    private static final DecimalFormat PMP_FORMAT_CALCULATE = new DecimalFormat("#.##");
	private static final double BK_PERCENT = 75.0;
	
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
		sample.setStatus(StatusType.SUCCESS);
		
		sample.getMeasurementPmpList().addAll(buildMeasurementPmp(sampleHelper.getPmpList(), sample, model));
		sample.getMeasurementFmList().addAll(buildMeasurementFm(sampleHelper.getFmList(),sample.getMeasurementPmpList(), sample, model));
		
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
		sampleDTO.setFmIndicator(buildFmIndicator(sampleDTO.getMeasurementFmList()));
		sampleDTO.setPmpIndicator(buildPmpIndicator(sampleDTO.getMeasurementPmpList()));
				
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
	
	private	FmIndicator buildFmIndicator(Set<MeasurementFmDTO> fmMeasurementDTOList) {
		FmIndicator fmIndicatorDTO = new FmIndicator();
		
		for (MeasurementFmDTO fmMeasurement : fmMeasurementDTOList) {
			double defaultHigherToleranceValueRounded = Double.parseDouble(FM_FORMAT_CALCULATE.format(fmMeasurement.getHigherTolerance()));
			double defaultlowerToleranceValueRounded = Double.parseDouble(FM_FORMAT_CALCULATE.format(fmMeasurement.getLowerTolerance()));

			double defaultLimitedHigherToleranceValueRounded = BK_PERCENT*defaultHigherToleranceValueRounded;
			double defaultLimitedLlowerToleranceValueRounded = BK_PERCENT*defaultlowerToleranceValueRounded;

			double value = Double.parseDouble(FM_FORMAT_CALCULATE.format(fmMeasurement.getValue()-fmMeasurement.getDefaultValue()));
			
			boolean isAk = value > defaultHigherToleranceValueRounded || value < defaultlowerToleranceValueRounded;
			boolean isBk = !isAk && ((value <= defaultHigherToleranceValueRounded && value>=defaultLimitedHigherToleranceValueRounded) || 
					(value <= defaultlowerToleranceValueRounded && value <= defaultLimitedLlowerToleranceValueRounded));
			
			if (isAk) { fmIndicatorDTO.setAk(fmIndicatorDTO.getAk()+1); }
			else if (isBk) { fmIndicatorDTO.setBk(fmIndicatorDTO.getBk()+1); }
			else { fmIndicatorDTO.setIo(fmIndicatorDTO.getIo()+1); }
		}
		
		return fmIndicatorDTO;
	}
	
	private PmpIndicator buildPmpIndicator(Set<MeasurementPmpDTO> measurementPmpDTOList) {		
		PmpIndicator pmpIndicator = new PmpIndicator();
		
		for (MeasurementPmpDTO pmpMeasurement : measurementPmpDTOList) {
			
			for (MeasurementAxisCoordinateDTO measurementAxisCoordinate : pmpMeasurement.getMeasurementAxisCoordinateList()) {					
				double defaultHigherToleranceValueRounded = Double.parseDouble(PMP_FORMAT_CALCULATE.format(measurementAxisCoordinate.getHigherTolerance()));
				double defaultlowerToleranceValueRounded = Double.parseDouble(PMP_FORMAT_CALCULATE.format(measurementAxisCoordinate.getLowerTolerance()));

				double defaultLimitedHigherToleranceValueRounded = BK_PERCENT*defaultHigherToleranceValueRounded;
				double defaultLimitedLlowerToleranceValueRounded = BK_PERCENT*defaultlowerToleranceValueRounded;
				
				double value = Double.parseDouble(PMP_FORMAT_CALCULATE.format(measurementAxisCoordinate.getValue().doubleValue()));
				
				boolean isAk = value > defaultHigherToleranceValueRounded || value < defaultlowerToleranceValueRounded;
				boolean isBk = !isAk && ((value <= defaultHigherToleranceValueRounded && value>=defaultLimitedHigherToleranceValueRounded) || 
						(value <= defaultlowerToleranceValueRounded && value <= defaultLimitedLlowerToleranceValueRounded));
								
				if (measurementAxisCoordinate.getAxis().equals(AxisType.T)) {
					if (isAk) { pmpIndicator.setAkT(pmpIndicator.getAkT()+1);}
					else if(isBk) { pmpIndicator.setBkT(pmpIndicator.getBkT()+1); }
					else { pmpIndicator.setIoT(pmpIndicator.getIoT()+1); }
				}else if(measurementAxisCoordinate.getAxis().equals(AxisType.D)) {
					if (isAk) { pmpIndicator.setAkD(pmpIndicator.getAkD()+1);}
					else if(isBk) { pmpIndicator.setBkD(pmpIndicator.getBkD()+1); }
					else { pmpIndicator.setIoD(pmpIndicator.getIoD()+1); }
				}else if (measurementAxisCoordinate.getAxis().equals(AxisType.Z)) {
					if (isAk) { pmpIndicator.setAkZ(pmpIndicator.getAkZ()+1);}
					else if(isBk) { pmpIndicator.setBkZ(pmpIndicator.getBkZ()+1); }
					else { pmpIndicator.setIoZ(pmpIndicator.getIoZ()+1); }
				}else if (measurementAxisCoordinate.getAxis().equals(AxisType.Y)) {
					if (isAk) { pmpIndicator.setAkY(pmpIndicator.getAkY()+1);}
					else if(isBk) { pmpIndicator.setBkY(pmpIndicator.getBkY()+1); }
					else { pmpIndicator.setIoY(pmpIndicator.getIoY()+1); }
				}else {
					if (isAk) { pmpIndicator.setAkX(pmpIndicator.getAkX()+1);}
					else if(isBk) { pmpIndicator.setBkX(pmpIndicator.getBkX()+1); }
					else { pmpIndicator.setIoX(pmpIndicator.getIoX()+1); }
				}
					
			}
		}
		
		return pmpIndicator;
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
		
		for (FmHelper fmHelper : fmListHelper) {
			
			NominalFm nominalFm = model.getFmByName(fmHelper.getName());
			
			if (nominalFm == null) { throw new SampleException(SampleMessage.FM_NOT_FOUND+":"+fmHelper.getName()); }
			
			MeasurementFm measurementFm = new MeasurementFm();
			measurementFm.setNominalFm(nominalFm);
			logger.info("Measurement Fm Name: "+measurementFm.getNominalFm().getName());
			measurementFm.setSample(sample);
			measurementFm.setToleranceType(ToleranceType.valueOf(fmHelper.getMeasurementAxisCoordinates().getType().name()));
			logger.info("Measurement Fm Tolarance Type: "+measurementFm.getToleranceType());
			measurementFm.setValue(new BigDecimal(fmHelper.getMeasurementAxisCoordinates().getCalculated()));
			logger.info("Measurement Fm Value: "+measurementFm.getValue());
			
			logger.info("Fm_Pmp List : ");
			for (String pmpName : fmHelper.getPmpNameList()) {
				MeasurementPmp measurementPmpFound = measurementPmpList.stream()
						.filter(measurementPmp -> measurementPmp.getNominalPmp().getName().equals(pmpName)).findFirst().orElse(null);
				
				if (measurementPmpFound == null) { 
					logger.info(SampleMessage.PMP_NOT_FOUND+":"+pmpName);
					continue;
				}
				
				logger.info("Pmp name: "+measurementPmpFound.getNominalPmp().getName());
				
				measurementFm.getMeasurementPmpList().add(measurementPmpFound);
			}
			
			measurementFmList.add(measurementFm);
		}
		
		return measurementFmList;
	}
	
	private Set<MeasurementPmp> buildMeasurementPmp(List<PmpHelper> pmpListHelper, Sample sample, Model model) throws SampleException {
		Set<MeasurementPmp> measurementPmpList = new HashSet<>();
		
		for (PmpHelper pmpHelper : pmpListHelper) {
			
			NominalPmp nominalPmp = model.getPmpByName(pmpHelper.getName());
			
			if (nominalPmp == null) { throw new SampleException(SampleMessage.PMP_NOT_FOUND+":"+pmpHelper.getName()); }
			
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

			measurementPmp.setNominalPmp(nominalPmp);
			measurementPmp.setSample(sample);
			measurementPmp.setMeasurementAxisCoordinateList(buildMeasurementAxisCoordiante(pmpHelper.getMeasurementAxisCoordinates(), measurementPmp));

			measurementPmpList.add(measurementPmp);
		}
		
		return measurementPmpList;
	}
	
	private Set<MeasurementAxisCoordinate> buildMeasurementAxisCoordiante(
			List<MeasurementAxisCoordinateHelper> measurementAxisCoordinateListHelper, MeasurementPmp measurementPmp) throws SampleException {
		Set<MeasurementAxisCoordinate> measurementAxisCoordinateList = new HashSet<>();
			
		NominalPmp nominalPmp = measurementPmp.getNominalPmp();
		
		for (MeasurementAxisCoordinateHelper measurementAxisCoordinateHelper : measurementAxisCoordinateListHelper) {
			
			NominalAxisCoordinate nominalAxisCoordinate = nominalPmp.getAxisCoordinateByName(measurementAxisCoordinateHelper.getName());
			if (nominalAxisCoordinate == null) { throw new SampleException(SampleMessage.AXIS_COORDINATE_NOT_FOUND+":"+measurementAxisCoordinateHelper.getName()); }
						
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
