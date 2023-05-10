package com.fauv.analyzer.entity.statistics;

import java.util.ArrayList;
import java.util.List;

import com.fauv.analyzer.enums.CatalogType;

public class FmStatistic {

	private String name;
	private CatalogType catalogType;
	private int totalIo;
	private int totalBk;
	private int totalAk;
	private double percentageAk;
	private double percentageBk;
	private double percentageIo;
	private double cp;
	private double cpk;
	private double pp;
	private double ppk;
	private double standardDeviation;
	private double sigmaLevel;
	private double average;
	private double nominalDistribution;
	private double z1;
	private double z2;
	private boolean isAble;
	private List<String> mappedPmpList = new ArrayList<>();
	private List<String> impactList = new ArrayList<>();
	private CepIndividualValuesGraphic cepIndividualValuesGraphic;
	private CepMovelAmplitudeGraphic cepMovelAmplitudeGraphic;
	private IndividualValuesGraphic individualValuesGraphic;
	private MovelAmplitudeGraphic movelAmplitudeGraphic;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CatalogType getCatalogType() {
		return catalogType;
	}
	
	public void setCatalogType(CatalogType catalogType) {
		this.catalogType = catalogType;
	}
	
	public int getTotalIo() {
		return totalIo;
	}

	public void setTotalIo(int totalIo) {
		this.totalIo = totalIo;
	}

	public int getTotalBk() {
		return totalBk;
	}

	public void setTotalBk(int totalBk) {
		this.totalBk = totalBk;
	}

	public int getTotalAk() {
		return totalAk;
	}

	public void setTotalAk(int totalAk) {
		this.totalAk = totalAk;
	}
	
	public double getPercentageAk() {
		return percentageAk;
	}

	public void setPercentageAk(double percentageAk) {
		this.percentageAk = percentageAk;
	}

	public double getPercentageBk() {
		return percentageBk;
	}

	public void setPercentageBk(double percentageBk) {
		this.percentageBk = percentageBk;
	}

	public double getPercentageIo() {
		return percentageIo;
	}

	public void setPercentageIo(double percentageIo) {
		this.percentageIo = percentageIo;
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
	
	public double getZ1() {
		return z1;
	}

	public void setZ1(double z1) {
		this.z1 = z1;
	}

	public double getZ2() {
		return z2;
	}

	public void setZ2(double z2) {
		this.z2 = z2;
	}
	
	public boolean isAble() {
		return isAble;
	}

	public void setAble(boolean isAble) {
		this.isAble = isAble;
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
