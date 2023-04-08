package com.fauv.analyzer.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fauv.analyzer.enums.StatusType;
import com.fauv.analyzer.enums.ToleranceType;
import com.fauv.analyzer.message.SampleMessage;

@Entity
@Table(name = "sample", schema = "analyzer")
public class Sample {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = SampleMessage.UPLOAD_DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime uploadedDate;
	
	@NotBlank(message = SampleMessage.UPLOAD_DATE)
	@Size(max = 45, min = 1, message = SampleMessage.UPLOAD_USER_SIZE)
	private String uploadedUser;
	
	@NotNull(message = SampleMessage.MODEL)
	@ManyToOne
	@JoinColumn(name = "model_id")
	private Model model;
	
	@NotNull(message = SampleMessage.EQUIPMENT)
	@ManyToOne
	@JoinColumn(name = "equipment_id")
	private Equipment equipment;
	
	@NotBlank(message = SampleMessage.PIN)
	@Size(max = 10, min = 1, message = SampleMessage.PIN_SIZE)
	private String pin;
	
	@NotNull(message = SampleMessage.SCAN_INIT_DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime scanInitDate;
	
	@NotNull(message = SampleMessage.SCAN_END_DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime scanEndDate;
	
	@NotNull(message = SampleMessage.STATUS)
	@Enumerated(EnumType.STRING)
	private StatusType status;
	
	private String fileName;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "sample_id")
	private Set<MeasurementPmp> measurementPmpList = new HashSet<>(); 

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "sample_id")
	private Set<MeasurementFm> measurementFmList = new HashSet<>();
	
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
	
	public void setUploadedDate(LocalDate uploadedDate, LocalTime uploadedTime) {
		if (uploadedDate == null || uploadedTime == null)  { return; }
		
		this.uploadedDate = LocalDateTime.of(uploadedDate, uploadedTime);
	}

	public String getUploadedUser() {
		return uploadedUser;
	}

	public void setUploadedUser(String uploadedUser) {
		this.uploadedUser = uploadedUser;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
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
	
	public void setScanInitDate(LocalDate scanInitDate, LocalTime scanInitTime) {
		if (scanInitDate == null || scanInitTime == null)  { return; }
	
		this.scanInitDate = LocalDateTime.of(scanInitDate, scanInitTime);
	}

	public LocalDateTime getScanEndDate() {
		return scanEndDate;
	}

	public void setScanEndDate(LocalDateTime scanEndDate) {
		this.scanEndDate = scanEndDate;
	}
	
	public void setScanEndDate(LocalDate scanEndDate, LocalTime scanEndTime) {
		if (scanInitDate == null || scanEndTime == null)  { return; }
		
		this.scanEndDate = LocalDateTime.of(scanEndDate, scanEndTime);;
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

	public Set<MeasurementPmp> getMeasurementPmpList() {
		return measurementPmpList;
	}

	public void setMeasurementPmpList(Set<MeasurementPmp> measurementPmpList) {
		this.measurementPmpList = measurementPmpList;
	}

	public Set<MeasurementFm> getMeasurementFmList() {
		return measurementFmList;
	}

	public void setMeasurementFmList(Set<MeasurementFm> measurementFmList) {
		this.measurementFmList = measurementFmList;
	}
	
	public Set<String> fmsOutTol() {
		return getMeasurementFmList().stream().filter(fm -> fm.getToleranceType().equals(ToleranceType.OUTOL))
				.map(fm -> fm.getNominalFm().getName()).collect(Collectors.toSet());
	}
	
	public Set<String> fmsIntTol() {
		return getMeasurementFmList().stream().filter(fm -> fm.getToleranceType().equals(ToleranceType.INTOL))
				.map(fm -> fm.getNominalFm().getName()).collect(Collectors.toSet());
	}
	
	
}
