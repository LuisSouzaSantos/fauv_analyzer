package com.fauv.analyzer.entity.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fauv.analyzer.enums.StatusType;

public class SampleOverview {

	private LocalDateTime uploadedDate;
	private String uploadedUser;
	private String equipmentName;
	private String partNumber;
	private String carName;
	private String pin;
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonFormat(pattern="dd/MM/yyyy HH:mm:ss")
	private LocalDateTime initDate;
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonFormat(pattern="dd/MM/yyyy HH:mm:ss")
	private LocalDateTime endDate;
	private StatusType status;
	private int totalFmAk;
	private int totalFmBk;
	private int totalFmIo;
	private int totalPmpAk;
	private int totalPmpBk;
	private int totalPmpIo;
	private List<String> fmListAk = new ArrayList<>();
	private List<FmOverview> fmOverviewList = new ArrayList<>();
	private List<PmpOverview> pmpOverviewList = new ArrayList<>();
	
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
	public String getEquipmentName() {
		return equipmentName;
	}
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	public String getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	public String getCarName() {
		return carName;
	}
	public void setCarName(String carName) {
		this.carName = carName;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public LocalDateTime getInitDate() {
		return initDate;
	}
	public void setInitDate(LocalDateTime initDate) {
		this.initDate = initDate;
	}
	public LocalDateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}
	public StatusType getStatus() {
		return status;
	}
	public void setStatus(StatusType status) {
		this.status = status;
	}
	public int getTotalFmAk() {
		return totalFmAk;
	}
	public void setTotalFmAk(int totalFmAk) {
		this.totalFmAk = totalFmAk;
	}
	public int getTotalFmBk() {
		return totalFmBk;
	}
	public void setTotalFmBk(int totalFmBk) {
		this.totalFmBk = totalFmBk;
	}
	public int getTotalFmIo() {
		return totalFmIo;
	}
	public void setTotalFmIo(int totalFmIo) {
		this.totalFmIo = totalFmIo;
	}
	public int getTotalPmpAk() {
		return totalPmpAk;
	}
	public void setTotalPmpAk(int totalPmpAk) {
		this.totalPmpAk = totalPmpAk;
	}
	public int getTotalPmpBk() {
		return totalPmpBk;
	}
	public void setTotalPmpBk(int totalPmpBk) {
		this.totalPmpBk = totalPmpBk;
	}
	public int getTotalPmpIo() {
		return totalPmpIo;
	}
	public void setTotalPmpIo(int totalPmpIo) {
		this.totalPmpIo = totalPmpIo;
	}
	public List<String> getFmListAk() {
		return fmListAk;
	}
	public void setFmListAk(List<String> fmListAk) {
		this.fmListAk = fmListAk;
	}
	public List<FmOverview> getFmOverviewList() {
		return fmOverviewList;
	}
	public void setFmOverviewList(List<FmOverview> fmOverviewList) {
		this.fmOverviewList = fmOverviewList;
	}
	public List<PmpOverview> getPmpOverviewList() {
		return pmpOverviewList;
	}
	public void setPmpOverviewList(List<PmpOverview> pmpOverviewList) {
		this.pmpOverviewList = pmpOverviewList;
	}
	
}
