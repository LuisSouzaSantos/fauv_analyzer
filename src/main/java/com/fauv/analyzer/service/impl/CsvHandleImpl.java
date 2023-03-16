package com.fauv.analyzer.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fauv.analyzer.entity.helper.FmImpactHelper;
import com.fauv.analyzer.service.CsvHandleService;

@Service
public class CsvHandleImpl implements CsvHandleService {
	
	private static final String SEPARATOR = ",";

	@Override
	public Set<FmImpactHelper> parseCsvFileToImpactHelpers(MultipartFile csvFile) throws Exception {		
		if (csvFile == null) { throw new Exception("CSV_FILE_CANNOT_BE_NULL"); }
		
        byte[] bytes = csvFile.getBytes();
        String fileContent = new String(bytes);
        
        //Split doc lines 
        String[] lines = fileContent.split("\n");

		Set<FmImpactHelper> fmImpactHelperList = new HashSet<>();
        		
        for (int i = 1; i < lines.length; i++) {
            String[] values = lines[i].split(SEPARATOR);
            
            FmImpactHelper fmImpactHelper = new FmImpactHelper();
            fmImpactHelper.setFmName(values[0]);
            
            for (int j = 1; j < values.length; j++) {
            	fmImpactHelper.getInformationList().add(values[j]);
			}

            fmImpactHelperList.add(fmImpactHelper);
        }

        return fmImpactHelperList;
	}

}

