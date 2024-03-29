package com.fauv.analyzer.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fauv.analyzer.entity.form.FmForm;
import com.fauv.analyzer.enums.AxisType;
import com.fauv.analyzer.enums.CatalogType;
import com.fauv.analyzer.enums.FmLevel;
import com.fauv.analyzer.message.ModelMessage;

@Entity
@Table(name = "nominal_fm", schema = "analyzer")
public class NominalFm {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = ModelMessage.NOMINAL_FM_NAME)
	private String name;
	
	@NotNull(message = ModelMessage.NOMINAL_FM_HIGHER_VALUE_TOLERANCE)
	private BigDecimal higherTolerance;
	
	@NotNull(message = ModelMessage.NOMINAL_FM_LOWER_VALUE_TOLERANCE)
	private BigDecimal lowerTolerance;
	
	@NotNull(message = ModelMessage.NOMINAL_FM_DEFAULT_VALUE)
	private BigDecimal defaultValue;
	
	@NotNull(message = ModelMessage.NOMINAL_FM_CATALOG)
	@Enumerated(EnumType.STRING)
	private CatalogType catalogType = CatalogType.DICHTIGKEIT;
	
	@NotNull(message = ModelMessage.NOMINAL_FM_AXIS)
	@Enumerated(EnumType.STRING)
	private AxisType axis = AxisType.X;
	
	@NotNull(message = ModelMessage.NOMINAL_FM_LEVEL)
	@Enumerated(EnumType.STRING)
	private FmLevel level = FmLevel.MEDIUM;
	private boolean active = true;
	
	@ManyToOne
    private Model model;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "nominalFm")
	private List<FmImpact> fmImpactList = new ArrayList<>();
	
	@ManyToMany
	@JoinTable(
	  name = "nominal_pmp_fm", 
	  joinColumns = @JoinColumn(name = "nominal_fm_id"), 
	  inverseJoinColumns = @JoinColumn(name = "nominal_pmp_id"))
	private List<NominalPmp> pmpList = new ArrayList<>();
	
	@OneToMany(mappedBy = "nominalFm")
	private List<MeasurementFm> measurementFmList = new ArrayList<>();
	
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
	
	public BigDecimal getHigherTolerance() {
		return higherTolerance;
	}

	public void setHigherTolerance(BigDecimal higherTolerance) {
		this.higherTolerance = higherTolerance;
	}

	public BigDecimal getLowerTolerance() {
		return lowerTolerance;
	}

	public void setLowerTolerance(BigDecimal lowerTolerance) {
		this.lowerTolerance = lowerTolerance;
	}

	public BigDecimal getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(BigDecimal defaultValue) {
		this.defaultValue = defaultValue;
	}

	public CatalogType getCatalogType() {
		return catalogType;
	}

	public void setCatalogType(CatalogType catalogType) {
		this.catalogType = catalogType;
	}

	public AxisType getAxis() {
		return axis;
	}

	public void setAxis(AxisType axis) {
		this.axis = axis;
	}

	public FmLevel getLevel() {
		return level;
	}

	public void setLevel(FmLevel level) {
		this.level = level;
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

	public List<FmImpact> getFmImpactList() {
		return fmImpactList;
	}

	public void setFmImpactList(List<FmImpact> fmImpactList) {
		this.fmImpactList = fmImpactList;
	}

	public List<NominalPmp> getPmpList() {
		return pmpList;
	}

	public void setPmpList(List<NominalPmp> pmpList) {
		this.pmpList = pmpList;
	}
	
	public List<MeasurementFm> getMeasurementFmList() {
		return measurementFmList;
	}

	public void setMeasurementFmList(List<MeasurementFm> measurementFmList) {
		this.measurementFmList = measurementFmList;
	}
	
	public boolean hasMeasurementFms() {
		return getMeasurementFmList() != null && !getMeasurementFmList().isEmpty();
	}

	public List<String> getFmImpactListString() {
		return this.getFmImpactList().stream().map(impact -> impact.getInfo()).collect(Collectors.toList());
	}
	
	public List<String> getMappedPmpList() {
		return getPmpList().stream().map(pmp -> pmp.getName()).collect(Collectors.toList());
	}

	public static NominalFm buildNominalFm(FmForm fmForm) {
		NominalFm nominalFm = new NominalFm();
		nominalFm.setName(fmForm.getName());
		nominalFm.setActive(fmForm.isActive());
		nominalFm.setAxis(fmForm.getAxis());
		nominalFm.setDefaultValue(new BigDecimal(fmForm.getDefaultValue()));
		nominalFm.setLowerTolerance(new BigDecimal(fmForm.getLowerTolerance()));
		nominalFm.setHigherTolerance(new BigDecimal(fmForm.getHigherTolerance()));
		nominalFm.setLevel(fmForm.getLevel());
			
		return nominalFm;
	}

	public static NominalFm buildNominalFm(NominalFm previousNominalFm, Model model) {
		NominalFm nominalFm = new NominalFm();
		nominalFm.setName(previousNominalFm.getName());
		nominalFm.setActive(previousNominalFm.isActive());
		nominalFm.setAxis(previousNominalFm.getAxis());
		nominalFm.setDefaultValue(previousNominalFm.getDefaultValue());
		nominalFm.setLowerTolerance(previousNominalFm.getLowerTolerance());
		nominalFm.setHigherTolerance(previousNominalFm.getHigherTolerance());
		nominalFm.setLevel(previousNominalFm.getLevel());
		nominalFm.setModel(model);
			
		return nominalFm;
	}

}
