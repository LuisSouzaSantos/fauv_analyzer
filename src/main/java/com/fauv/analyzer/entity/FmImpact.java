package com.fauv.analyzer.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fauv.analyzer.entity.form.FmImpactForm;

@Entity
@Table(name = "fm_impact", schema = "analyzer")
public class FmImpact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String info;
	@ManyToOne
    private NominalFm nominalFm;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getInfo() {
		return info;
	}
	
	public void setInfo(String info) {
		this.info = info;
	}
	
	public NominalFm getNominalFm() {
		return nominalFm;
	}

	public void setNominalFm(NominalFm nominalFm) {
		this.nominalFm = nominalFm;
	}

	public static FmImpact buildFmImpact(FmImpactForm form) {
		FmImpact fmImpact = new FmImpact();
		
		fmImpact.setInfo(form.getInfo());
		
		return fmImpact;
	}

	public static FmImpact buildFmImpact(FmImpact previousfmImpact, NominalFm nominalFm) {
		FmImpact fmImpact = new FmImpact();
		
		fmImpact.setInfo(previousfmImpact.getInfo());
		fmImpact.setNominalFm(nominalFm);
		
		return fmImpact;
	}	
	
	
}
