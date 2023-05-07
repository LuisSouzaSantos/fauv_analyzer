package com.fauv.analyzer.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.fauv.analyzer.entity.dto.UserDTO;

public interface AuthenticationService {
	
	public void authenticateOnServer(UserDTO user);
	
	public UserDetails loadUserByUsername(String tokenValue) throws UsernameNotFoundException;
	
}
