package com.fauv.analyzer.entity.dto;

import java.util.ArrayList;
import java.util.List;

public class PmpOverview {

	private String name;
	private double x;
	private double y;
	private double z;
	private List<String> fmList;
	private int ak;
	private int bk;
	private int io;
	private List<AxisCoordinateOverview> axisCoordinateOverviewList = new ArrayList<>();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public double getZ() {
		return z;
	}
	
	public void setZ(double z) {
		this.z = z;
	}
	
	public List<String> getFmList() {
		return fmList;
	}
	
	public void setFmList(List<String> fmList) {
		this.fmList = fmList;
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
	
	public List<AxisCoordinateOverview> getAxisCoordinateOverviewList() {
		return axisCoordinateOverviewList;
	}
	
	public void setAxisCoordinateOverviewList(List<AxisCoordinateOverview> axisCoordinateOverviewList) {
		this.axisCoordinateOverviewList = axisCoordinateOverviewList;
	}
	
}
