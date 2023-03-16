package com.fauv.analyzer.entity.helper;

import java.util.ArrayList;
import java.util.List;

public class FmImpactHelper {

	private String fmName;
	private List<String> informationList = new ArrayList<>();
	
	public String getFmName() {
		return fmName;
	}
	
	public void setFmName(String fmName) {
		this.fmName = fmName;
	}

	public List<String> getInformationList() {
		return informationList;
	}

	public void setInformationList(List<String> informationList) {
		this.informationList = informationList;
	}
	
}
