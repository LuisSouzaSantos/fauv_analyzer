package com.fauv.analyzer.entity.dto;

import java.util.ArrayList;
import java.util.List;

public class DetailedFmStatisticDTO {

	private String fmName;
	private double totalOfIo;
	private double totalOfBk;
	private double totalOfAk;
	private List<String> pmpList = new ArrayList<>();
	private double cp;
	private double cpk;
	private double pp;
	private double ppk;
	private double sigmaLevel;
	private double average;
	private double nominalDistribution;
	private List<String> impactList = new ArrayList<>();
	private List<SampleFmDetailedStatisticDTO> sampleFmDetailedStatisticDTOList = new ArrayList<>();
	
	public String getFmName() {
		return fmName;
	}
	
	public void setFmName(String fmName) {
		this.fmName = fmName;
	}
	
	public double getTotalOfIo() {
		return totalOfIo;
	}
	
	public void setTotalOfIo(double totalOfIo) {
		this.totalOfIo = totalOfIo;
	}
	
	public double getTotalOfBk() {
		return totalOfBk;
	}
	
	public void setTotalOfBk(double totalOfBk) {
		this.totalOfBk = totalOfBk;
	}
	
	public double getTotalOfAk() {
		return totalOfAk;
	}
	
	public void setTotalOfAk(double totalOfAk) {
		this.totalOfAk = totalOfAk;
	}
	
	public List<String> getPmpList() {
		return pmpList;
	}
	
	public void setPmpList(List<String> pmpList) {
		this.pmpList = pmpList;
	}
	
	public double getCp() {
		return cp;
	}
	
	public void setCp(double cp) {
		this.cp = cp;
	}
	
	public double getCpk() {
		return cpk;
	}
	
	public void setCpk(double cpk) {
		this.cpk = cpk;
	}
	
	public double getPp() {
		return pp;
	}
	
	public void setPp(double pp) {
		this.pp = pp;
	}
	
	public double getPpk() {
		return ppk;
	}
	
	public void setPpk(double ppk) {
		this.ppk = ppk;
	}
	
	public double getSigmaLevel() {
		return sigmaLevel;
	}
	
	public void setSigmaLevel(double sigmaLevel) {
		this.sigmaLevel = sigmaLevel;
	}
	
	public double getAverage() {
		return average;
	}
	
	public void setAverage(double average) {
		this.average = average;
	}
	
	public double getNominalDistribution() {
		return nominalDistribution;
	}
	
	public void setNominalDistribution(double nominalDistribution) {
		this.nominalDistribution = nominalDistribution;
	}
	
	public List<String> getImpactList() {
		return impactList;
	}
	
	public void setImpactList(List<String> impactList) {
		this.impactList = impactList;
	}
	
	public List<SampleFmDetailedStatisticDTO> getSampleFmDetailedStatisticDTOList() {
		return sampleFmDetailedStatisticDTOList;
	}
	
	public void setSampleFmDetailedStatisticDTOList(List<SampleFmDetailedStatisticDTO> sampleFmDetailedStatisticDTOList) {
		this.sampleFmDetailedStatisticDTOList = sampleFmDetailedStatisticDTOList;
	}
	
}
