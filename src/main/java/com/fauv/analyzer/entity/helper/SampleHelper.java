package com.fauv.analyzer.entity.helper;

import java.util.ArrayList;
import java.util.List;

public class SampleHelper {

	private HeaderHelper header;
	private List<PmpHelper> pmpList = new ArrayList<>();
	private List<FmHelper> fmList = new ArrayList<>();
	
	public HeaderHelper getHeader() {
		return header;
	}
	
	public void setHeader(HeaderHelper header) {
		this.header = header;
	}
	
	public List<PmpHelper> getPmpList() {
		return pmpList;
	}
	
	public void setPmpList(List<PmpHelper> pmpList) {
		this.pmpList = pmpList;
	}
	
	public List<FmHelper> getFmList() {
		return fmList;
	}
	
	public void setFmList(List<FmHelper> fmList) {
		this.fmList = fmList;
	}
	
	public boolean isValid() {
		return this.getHeader() != null;
	}
		
}
