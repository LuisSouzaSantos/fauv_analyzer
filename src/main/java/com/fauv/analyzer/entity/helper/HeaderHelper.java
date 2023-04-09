package com.fauv.analyzer.entity.helper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import javax.validation.constraints.NotNull;

import com.fauv.analyzer.message.DMOMessage;

public class HeaderHelper {
	
	private String fileName;
	@NotNull(message = DMOMessage.HEADER_START_DATE_FIELD)
	private LocalDate startDate;
	@NotNull(message = DMOMessage.HEADER_START_TIME_FIELD)
	private LocalTime startTime;
	@NotNull(message = DMOMessage.HEADER_END_DATE_FIELD)
	private LocalDate endDate;
	@NotNull(message = DMOMessage.HEADER_END_TIME_FIELD)
	private LocalTime endTime;
	@NotNull(message = DMOMessage.HEADER_PART_NUMBER_FIELD)
	private String partNumber;
	@NotNull(message = DMOMessage.HEADER_EQUIPMENT_NAME_FIELD)
	private String equipmentName;
	@NotNull(message = DMOMessage.HEADER_SAMPLE_ID_FIELD)
	private String sampleId;
	@NotNull(message = DMOMessage.HEADER_INSPECTOR_FIELD)
	private String inspectorName;
	private ZoneId zoneId;
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public LocalDate getStartDate() {
		return startDate;
	}
	
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
		
	public LocalTime getStartTime() {
		return startTime;
	}
	
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	
	public LocalDate getEndDate() {
		return endDate;
	}
	
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	public LocalTime getEndTime() {
		return endTime;
	}
	
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
	
	public String getPartNumber() {
		return partNumber;
	}
	
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	
	public String getEquipmentName() {
		return equipmentName;
	}
	
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	
	public String getSampleId() {
		return sampleId;
	}
	
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}
	
	public String getInspectorName() {
		return inspectorName;
	}
	
	public void setInspectorName(String inspectorName) {
		this.inspectorName = inspectorName;
	}
	
	public ZoneId getZoneId() {
		return zoneId;
	}
	
	public void setZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
	}

}
