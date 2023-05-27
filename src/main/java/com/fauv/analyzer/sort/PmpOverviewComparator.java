package com.fauv.analyzer.sort;

import java.util.Comparator;

import com.fauv.analyzer.entity.dto.PmpOverview;

public class PmpOverviewComparator implements Comparator<PmpOverview> {

	@Override
	public int compare(PmpOverview pmpOverview1, PmpOverview pmpOverview2) {
		if (pmpOverview1.getAk() > pmpOverview2.getAk()) { return -1; }
		if (pmpOverview2.getAk() < pmpOverview1.getAk()) { return 1; }
		if (pmpOverview1.getAk() == pmpOverview2.getAk()) {
			if (pmpOverview1.getBk() == pmpOverview2.getBk()) { 
				if (pmpOverview1.getIo() > pmpOverview2.getIo()) { return -1; }
				if (pmpOverview2.getIo() < pmpOverview1.getIo()) { return 1; }
				return 0;
			}
			
			if (pmpOverview1.getBk() > pmpOverview2.getBk()) { return -1; }
			if (pmpOverview2.getBk() < pmpOverview1.getBk()) { return 1; }
		}

		return 0;
	}

}
