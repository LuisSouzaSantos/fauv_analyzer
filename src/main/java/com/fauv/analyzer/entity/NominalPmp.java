package com.fauv.analyzer.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fauv.analyzer.entity.dto.PmpDTO;
import com.fauv.analyzer.entity.form.PmpForm;
import com.fauv.analyzer.message.ModelMessage;

@Entity
@Table(name = "nominal_pmp", schema = "analyzer")
public class NominalPmp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = ModelMessage.NOMINAL_PMP_NAME)
	private String name;
	
	@NotNull(message = ModelMessage.NOMINAL_PMP_X)
	private BigDecimal x;
	
	@NotNull(message = ModelMessage.NOMINAL_PMP_Y)
	private BigDecimal y;
	
	@NotNull(message = ModelMessage.NOMINAL_PMP_Z)
	private BigDecimal z;
	
	private boolean active = true;
	
	@ManyToOne
    private Model model;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "nominal_pmp_id")
	private List<NominalAxisCoordinate> axisCoordinateList = new ArrayList<>();
	
	@OneToMany(mappedBy = "nominalPmp")
	private List<MeasurementPmp> measurementPmpList = new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getX() {
		return x;
	}

	public void setX(BigDecimal x) {
		this.x = x;
	}

	public BigDecimal getY() {
		return y;
	}

	public void setY(BigDecimal y) {
		this.y = y;
	}

	public BigDecimal getZ() {
		return z;
	}

	public void setZ(BigDecimal z) {
		this.z = z;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public List<NominalAxisCoordinate> getAxisCoordinateList() {
		return axisCoordinateList;
	}
	
	public void setAxisCoordinateList(List<NominalAxisCoordinate> axisCoordinateList) {
		this.axisCoordinateList = axisCoordinateList;
	}
	
	public List<MeasurementPmp> getMeasurementPmpList() {
		return measurementPmpList;
	}

	public void setMeasurementPmpList(List<MeasurementPmp> measurementPmpList) {
		this.measurementPmpList = measurementPmpList;
	}
	
	public boolean hasMeasurementPmps() {
		return getMeasurementPmpList() != null && !getMeasurementPmpList().isEmpty();
	}

	public NominalAxisCoordinate getAxisCoordinateByName(String name) {
		return getAxisCoordinateList().stream().filter(axisCoordinate -> axisCoordinate.getName().equals(name)).findFirst().orElse(null);
	}

	public static NominalPmp buildNominalPmp(PmpForm pmpForm) {
		NominalPmp nominalPmp = new NominalPmp();
		nominalPmp.setActive(pmpForm.isActive());
		nominalPmp.setName(pmpForm.getName());
		nominalPmp.setX(new BigDecimal(pmpForm.getX()));
		nominalPmp.setY(new BigDecimal(pmpForm.getY()));
		nominalPmp.setZ(new BigDecimal(pmpForm.getZ()));
		
		nominalPmp.setAxisCoordinateList(pmpForm.getAxisCoordinateList().stream()
				.map(axisCoordinate -> NominalAxisCoordinate.build(axisCoordinate, nominalPmp))
				.collect(Collectors.toList()));
		
		return nominalPmp;
	}
	
	public static NominalPmp buildNominalPmp(PmpDTO pmpDTO) {
		NominalPmp nominalPmp = new NominalPmp();
		nominalPmp.setActive(pmpDTO.isActive());
		nominalPmp.setName(pmpDTO.getName());
		nominalPmp.setX(new BigDecimal(pmpDTO.getX()));
		nominalPmp.setY(new BigDecimal(pmpDTO.getY()));
		nominalPmp.setZ(new BigDecimal(pmpDTO.getZ()));
		
		nominalPmp.setAxisCoordinateList(pmpDTO.getAxisCoordinateList().stream()
				.map(axisCoordinate -> NominalAxisCoordinate.build(axisCoordinate, nominalPmp))
				.collect(Collectors.toList()));
		
		return nominalPmp;
	}
	
	public static NominalPmp buildNominalPmp(NominalPmp previousNominalPmp, Model model) {
		NominalPmp nominalPmp = new NominalPmp();
		nominalPmp.setActive(previousNominalPmp.isActive());
		nominalPmp.setName(previousNominalPmp.getName());
		nominalPmp.setX(previousNominalPmp.getX());
		nominalPmp.setY(previousNominalPmp.getY());
		nominalPmp.setZ(previousNominalPmp.getZ());
		nominalPmp.setModel(model);
		
		nominalPmp.setAxisCoordinateList(previousNominalPmp.getAxisCoordinateList().stream()
				.map(axisCoordinate -> NominalAxisCoordinate.build(axisCoordinate, nominalPmp))
				.collect(Collectors.toList()));
		
		return nominalPmp;
	}
	
}
