package com.fauv.analyzer.entity.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fauv.analyzer.entity.indicators.FmIndicator;
import com.fauv.analyzer.entity.indicators.PmpIndicator;
import com.fauv.analyzer.enums.StatusType;

public class SampleDTO {

	private Long id;
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonFormat(pattern="dd/MM/yyyy HH:mm:ss")
	private LocalDateTime uploadedDate;
	private String uploadedUser;
	private EquipmentDTO equipment;
	@JsonIgnoreProperties(value = {"pmpList", "fmList"})
	private ModelDTO model;
	private String pin;
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonFormat(pattern="dd/MM/yyyy HH:mm:ss")
	private LocalDateTime scanInitDate;
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonFormat(pattern="dd/MM/yyyy HH:mm:ss")
	private LocalDateTime scanEndDate;
	private StatusType status;
	private String fileName;
	private Set<MeasurementPmpDTO> measurementPmpList = new HashSet<>(); 
	private Set<MeasurementFmDTO> measurementFmList = new HashSet<>();
	private FmIndicator fmIndicator;
	private PmpIndicator pmpIndicator;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public LocalDateTime getUploadedDate() {
		return uploadedDate;
	}
	
	public void setUploadedDate(LocalDateTime uploadedDate) {
		this.uploadedDate = uploadedDate;
	}
	
	public String getUploadedUser() {
		return uploadedUser;
	}
	
	public void setUploadedUser(String uploadedUser) {
		this.uploadedUser = uploadedUser;
	}
	
	public EquipmentDTO getEquipment() {
		return equipment;
	}
	
	public void setEquipment(EquipmentDTO equipment) {
		this.equipment = equipment;
	}
	
	public ModelDTO getModel() {
		return model;
	}
	
	public void setModel(ModelDTO model) {
		this.model = model;
	}
	
	public String getPin() {
		return pin;
	}
	
	public void setPin(String pin) {
		this.pin = pin;
	}
	
	public LocalDateTime getScanInitDate() {
		return scanInitDate;
	}
	
	public void setScanInitDate(LocalDateTime scanInitDate) {
		this.scanInitDate = scanInitDate;
	}
	
	public LocalDateTime getScanEndDate() {
		return scanEndDate;
	}
	
	public void setScanEndDate(LocalDateTime scanEndDate) {
		this.scanEndDate = scanEndDate;
	}
	
	public StatusType getStatus() {
		return status;
	}
	
	public void setStatus(StatusType status) {
		this.status = status;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public Set<MeasurementPmpDTO> getMeasurementPmpList() {
		return measurementPmpList;
	}
	
	public void setMeasurementPmpList(Set<MeasurementPmpDTO> measurementPmpList) {
		this.measurementPmpList = measurementPmpList;
	}
	
	public Set<MeasurementFmDTO> getMeasurementFmList() {
		return measurementFmList;
	}
	
	public void setMeasurementFmList(Set<MeasurementFmDTO> measurementFmList) {
		this.measurementFmList = measurementFmList;
	}

	public FmIndicator getFmIndicator() {
		return fmIndicator;
	}

	public void setFmIndicator(FmIndicator fmIndicator) {
		this.fmIndicator = fmIndicator;
	}

	public PmpIndicator getPmpIndicator() {
		return pmpIndicator;
	}

	public void setPmpIndicator(PmpIndicator pmpIndicator) {
		this.pmpIndicator = pmpIndicator;
	}

}
