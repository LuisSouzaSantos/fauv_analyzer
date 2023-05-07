package com.fauv.analyzer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fauv.analyzer.entity.dto.UserDTO;
import com.fauv.analyzer.service.AuthenticationService;
import com.fauv.analyzer.service.http.AuthHttp;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private AuthHttp authHttp;
	
	@Override
	public void authenticateOnServer(UserDTO user) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	}

	@Override
	public UserDetails loadUserByUsername(String tokenValue) throws UsernameNotFoundException {
		return authHttp.whoAmI(tokenValue);
	}
	
}
