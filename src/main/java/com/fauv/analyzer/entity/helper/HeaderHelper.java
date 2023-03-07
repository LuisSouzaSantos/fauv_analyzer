package com.fauv.analyzer.entity.helper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public class HeaderHelper {

	private String fileName;
	private LocalDate startDate;
	private LocalTime startTime;
	private LocalDate endDate;
	private LocalTime endTime;
	private String partNumber;
	private String equipmentName;
	private String sampleId;
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
