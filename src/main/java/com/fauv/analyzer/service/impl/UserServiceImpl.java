package com.fauv.analyzer.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fauv.analyzer.entity.dto.UserDTO;
import com.fauv.analyzer.service.UserService;
import com.fauv.analyzer.service.http.AuthHttp;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private AuthHttp authHttp;
	
	@Autowired
	private HttpServletRequest request;
	
	@Override
	public UserDTO whoAmI(String tokenValue) {
		if (tokenValue == null) { tokenValue = request.getHeader("Authorization"); }
		
		return authHttp.whoAmI(tokenValue);
	}

}
