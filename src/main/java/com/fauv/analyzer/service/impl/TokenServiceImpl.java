package com.fauv.analyzer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fauv.analyzer.entity.dto.TokenValidationResponse;
import com.fauv.analyzer.service.TokenService;
import com.fauv.analyzer.service.http.AuthHttp;

@Service
public class TokenServiceImpl implements TokenService {

	@Autowired
	private AuthHttp authHttp;
	
	@Override
	public boolean isValid(String token) {
		if (token == null || token.trim().isEmpty()) { return false; }
		
		TokenValidationResponse tokenValidationResponse = authHttp.validateToken(token);
		
		if (tokenValidationResponse == null) { return false; }
		
		return tokenValidationResponse.isValid();
	}

}
