package com.fauv.analyzer.service.impl;

import java.util.Comparator;

import com.fauv.analyzer.entity.dto.FmOverview;
import com.fauv.analyzer.enums.ToleranceTypeStatus;

public class FmOverviewComparator implements Comparator<FmOverview> {

	@Override
	public int compare(FmOverview fmOverview1, FmOverview fmOverview2) {
		if (fmOverview1.getToleranceStatus().equals(ToleranceTypeStatus.AK) && fmOverview2.getToleranceStatus().equals(ToleranceTypeStatus.AK)) {
			return 0;
		}
		
		if (fmOverview1.getToleranceStatus().equals(ToleranceTypeStatus.IO) && fmOverview2.getToleranceStatus().equals(ToleranceTypeStatus.IO)) {
			return 0;
		}
		
		if (fmOverview1.getToleranceStatus().equals(ToleranceTypeStatus.BK) && fmOverview2.getToleranceStatus().equals(ToleranceTypeStatus.BK)) {
			return 0;
		}
		
		if (fmOverview1.getToleranceStatus().equals(ToleranceTypeStatus.AK) && fmOverview2.getToleranceStatus().equals(ToleranceTypeStatus.BK)) {
			return -1;
		}
		
		if (fmOverview1.getToleranceStatus().equals(ToleranceTypeStatus.AK) && fmOverview2.getToleranceStatus().equals(ToleranceTypeStatus.IO)){
			return -1;
		}
		
		if (fmOverview1.getToleranceStatus().equals(ToleranceTypeStatus.BK) && fmOverview2.getToleranceStatus().equals(ToleranceTypeStatus.AK)){
			return 1;
		}
		
		if (fmOverview1.getToleranceStatus().equals(ToleranceTypeStatus.BK) && fmOverview2.getToleranceStatus().equals(ToleranceTypeStatus.IO)){
			return -1;
		}
		
		if (fmOverview1.getToleranceStatus().equals(ToleranceTypeStatus.IO) && fmOverview2.getToleranceStatus().equals(ToleranceTypeStatus.AK)){
			return 1;
		}
		
		if (fmOverview1.getToleranceStatus().equals(ToleranceTypeStatus.IO) && fmOverview2.getToleranceStatus().equals(ToleranceTypeStatus.BK)){
			return 1;
		}
		
		return 0;
	}

}
