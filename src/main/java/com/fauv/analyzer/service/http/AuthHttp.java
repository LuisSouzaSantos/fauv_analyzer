package com.fauv.analyzer.service.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fauv.analyzer.configuration.Properties;
import com.fauv.analyzer.entity.dto.TokenValidationRequest;
import com.fauv.analyzer.entity.dto.TokenValidationResponse;
import com.fauv.analyzer.entity.dto.UserDTO;

@Service
public class AuthHttp {

	@Autowired
	private Properties properties;
	
	public TokenValidationResponse validateToken(String tokenValue) {
		TokenValidationRequest tokenValidationRequest = new TokenValidationRequest();
		tokenValidationRequest.setValue(tokenValue);
		
		TokenValidationResponse response = null;
		
		RestTemplate restTemplate = new RestTemplate();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

		
        try {
			String url = properties.getBaseAuthUrl()+"/token/validate";
			
		    HttpHeaders headers = new HttpHeaders();
		    headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    
		    HttpEntity<String> request = 
		    	      new HttpEntity<String>(ow.writeValueAsString(tokenValidationRequest), headers);
		    	        
	        response = restTemplate.postForObject(url, request, TokenValidationResponse.class);
        }catch (HttpStatusCodeException e) {
        	System.out.println(e);
        } catch (Exception e) {
        	System.out.println(e);
        }
        
        return response;
	}
	
	public UserDTO whoAmI(String tokenValue) {
		UserDTO response = null;
		RestTemplate restTemplate = new RestTemplate();
		
        try {
			String url = properties.getBaseAuthUrl()+"/user/whoAmI";
			
		    HttpHeaders headers = new HttpHeaders();
		    headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.set("Authorization", tokenValue);
		    
		    HttpEntity<?> request = new HttpEntity<Object>(headers);

		    ResponseEntity<UserDTO> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, UserDTO.class);
		    		    	        
	        response = responseEntity.getBody();
        }catch (HttpStatusCodeException e) {
        	System.out.println(e);
        } catch (Exception e) {
        	System.out.println(e);
        }
        
        return response;
	}
	
}
