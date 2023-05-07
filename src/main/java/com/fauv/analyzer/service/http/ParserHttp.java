package com.fauv.analyzer.service.http;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fauv.analyzer.configuration.Properties;
import com.fauv.analyzer.entity.helper.SampleHelper;

@Service
public class ParserHttp {

	@Autowired
	private Properties properties;
	
	public SampleHelper readDmoFileAndBuildASample(MultipartFile dmoFile) {
        RestTemplate restTemplate = new RestTemplate();
        SampleHelper response = null;
        
        try {
			String parseUrl = properties.getBaseParserUrl()+"/dmo";
			
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		    LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		    
	        map.add("file", new MultipartInputStreamFileResource(dmoFile.getInputStream(), dmoFile.getOriginalFilename()));
	
	        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
	        
	        response = restTemplate.postForObject(parseUrl, requestEntity, SampleHelper.class);
        }catch (HttpStatusCodeException e) {
        	System.out.println(e);
        } catch (Exception e) {
        	System.out.println(e);
        }
        
        return response;
	}
	
	class MultipartInputStreamFileResource extends InputStreamResource {

	    private final String filename;

	    MultipartInputStreamFileResource(InputStream inputStream, String filename) {
	        super(inputStream);
	        this.filename = filename;
	    }

	    @Override
	    public String getFilename() {
	        return this.filename;
	    }

	    @Override
	    public long contentLength() throws IOException {
	        return -1; // we do not want to generally read the whole stream into memory ...
	    }
	}
	
	
}
