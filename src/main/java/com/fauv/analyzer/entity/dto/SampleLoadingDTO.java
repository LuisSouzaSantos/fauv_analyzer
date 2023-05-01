package com.fauv.analyzer.entity.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fauv.analyzer.enums.StatusType;

public class SampleLoadingDTO {

	private Long id;
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime uploadDate;
	@JsonIgnoreProperties(value = {"pmpList", "fmList"})
	private ModelDTO model;
	private EquipmentDTO equipment;
	private StatusType status;
	private String uploadUser;
	private int ak;
	private int bk;
	private int io;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public LocalDateTime getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(LocalDateTime uploadDate) {
		this.uploadDate = uploadDate;
	}

	public ModelDTO getModel() {
		return model;
	}
	
	public void setModel(ModelDTO model) {
		this.model = model;
	}

	public EquipmentDTO getEquipment() {
		return equipment;
	}

	public void setEquipment(EquipmentDTO equipment) {
		this.equipment = equipment;
	}

	public StatusType getStatus() {
		return status;
	}
	
	public void setStatus(StatusType status) {
		this.status = status;
	}
	
	public String getUploadUser() {
		return uploadUser;
	}
	
	public void setUploadUser(String uploadUser) {
		this.uploadUser = uploadUser;
	}
	
	public int getAk() {
		return ak;
	}
	
	public void setAk(int ak) {
		this.ak = ak;
	}
	
	public int getBk() {
		return bk;
	}
	
	public void setBk(int bk) {
		this.bk = bk;
	}
	
	public int getIo() {
		return io;
	}
	
	public void setIo(int io) {
		this.io = io;
	}

}
