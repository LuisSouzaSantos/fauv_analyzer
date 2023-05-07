package com.fauv.analyzer.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.web.filter.OncePerRequestFilter;

import com.fauv.analyzer.entity.dto.UserDTO;
import com.fauv.analyzer.service.AuthenticationService;
import com.fauv.analyzer.service.TokenService;
import com.fauv.analyzer.service.UserService;

public class AuthenticationFilterOnRequest extends OncePerRequestFilter {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	
	private AuthenticationService authenticationService;
	private TokenService tokenService;
	private UserService userService;
	
	public AuthenticationFilterOnRequest(AuthenticationService authenticationService, TokenService tokenService, UserService userService) {
		this.authenticationService = authenticationService;
		this.tokenService = tokenService;
		this.userService = userService;
	}
	
	@Transactional
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
 		String token = request.getHeader(AUTHORIZATION_HEADER);
		
		if (tokenService.isValid(token)) { tryingToAuthenticateTheUserOnServer(token); }
		
		filterChain.doFilter(request, response);
	}

	private void tryingToAuthenticateTheUserOnServer(String token) {		
		UserDTO user = userService.whoAmI(token);
		
		authenticationService.authenticateOnServer(user);
	}
	
	
}
