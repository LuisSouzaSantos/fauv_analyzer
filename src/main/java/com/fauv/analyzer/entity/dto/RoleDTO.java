package com.fauv.analyzer.entity.dto;

import org.springframework.security.core.GrantedAuthority;

public class RoleDTO implements GrantedAuthority {

	private static final long serialVersionUID = 1L;
	
	// administrator / inspector / consultant
	private String name;
	private boolean admin = false;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isAdmin() {
		return admin;
	}
	
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	@Override
	public String getAuthority() {
		return name;
	}
	
}
