package com.fauv.analyzer.entity.statistics;

import java.util.ArrayList;
import java.util.List;

import com.fauv.analyzer.enums.CatalogType;

public class FmStatistic {

	private String fmName;
	private CatalogType catalogType;
	private double totalIo;
	private double totalBk;
	private double totalAk;
	private double cp;
	private double cpk;
	private double pp;
	private double ppk;
	private double standardDeviation;
	private double sigmaLevel;
	private double average;
	private double nominalDistribution;
	private List<String> mappedPmpList = new ArrayList<>();
	private List<String> impactList = new ArrayList<>();
	private CepIndividualValuesGraphic cepIndividualValuesGraphic;
	private CepMovelAmplitudeGraphic cepMovelAmplitudeGraphic;
	private IndividualValuesGraphic individualValuesGraphic;
	private MovelAmplitudeGraphic movelAmplitudeGraphic;
	
	public String getFmName() {
		return fmName;
	}
	
	public void setFmName(String fmName) {
		this.fmName = fmName;
	}
	
	public CatalogType getCatalogType() {
		return catalogType;
	}
	
	public void setCatalogType(CatalogType catalogType) {
		this.catalogType = catalogType;
	}
	
	public double getTotalIo() {
		return totalIo;
	}
	
	public void setTotalIo(double totalIo) {
		this.totalIo = totalIo;
	}
	
	public double getTotalBk() {
		return totalBk;
	}
	
	public void setTotalBk(double totalBk) {
		this.totalBk = totalBk;
	}
	
	public double getTotalAk() {
		return totalAk;
	}
	
	public void setTotalAk(double totalAk) {
		this.totalAk = totalAk;
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
	
	public double getStandardDeviation() {
		return standardDeviation;
	}
	
	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
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
	
	public List<String> getMappedPmpList() {
		return mappedPmpList;
	}
	
	public void setMappedPmpList(List<String> mappedPmpList) {
		this.mappedPmpList = mappedPmpList;
	}
	
	public List<String> getImpactList() {
		return impactList;
	}
	
	public void setImpactList(List<String> impactList) {
		this.impactList = impactList;
	}
	
	public CepIndividualValuesGraphic getCepIndividualValuesGraphic() {
		return cepIndividualValuesGraphic;
	}
	
	public void setCepIndividualValuesGraphic(CepIndividualValuesGraphic cepIndividualValuesGraphic) {
		this.cepIndividualValuesGraphic = cepIndividualValuesGraphic;
	}
	
	public CepMovelAmplitudeGraphic getCepMovelAmplitudeGraphic() {
		return cepMovelAmplitudeGraphic;
	}
	
	public void setCepMovelAmplitudeGraphic(CepMovelAmplitudeGraphic cepMovelAmplitudeGraphic) {
		this.cepMovelAmplitudeGraphic = cepMovelAmplitudeGraphic;
	}
	
	public IndividualValuesGraphic getIndividualValuesGraphic() {
		return individualValuesGraphic;
	}
	
	public void setIndividualValuesGraphic(IndividualValuesGraphic individualValuesGraphic) {
		this.individualValuesGraphic = individualValuesGraphic;
	}
	
	public MovelAmplitudeGraphic getMovelAmplitudeGraphic() {
		return movelAmplitudeGraphic;
	}
	
	public void setMovelAmplitudeGraphic(MovelAmplitudeGraphic movelAmplitudeGraphic) {
		this.movelAmplitudeGraphic = movelAmplitudeGraphic;
	}
	
}
