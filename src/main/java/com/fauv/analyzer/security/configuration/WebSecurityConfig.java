package com.fauv.analyzer.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fauv.analyzer.security.filter.AuthenticationFilterOnRequest;
import com.fauv.analyzer.service.AuthenticationService;
import com.fauv.analyzer.service.TokenService;
import com.fauv.analyzer.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UserService userService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
			.cors()
			.and()
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests((auth) -> {
				auth.anyRequest().authenticated();
			})
			.headers(headers -> headers.xssProtection())
			.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(new AuthenticationFilterOnRequest(authenticationService, tokenService, userService), UsernamePasswordAuthenticationFilter.class)
			.build();
			
	}
	
}
