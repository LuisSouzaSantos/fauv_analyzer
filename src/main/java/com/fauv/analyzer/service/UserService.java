package com.fauv.analyzer.service;

import com.fauv.analyzer.entity.dto.UserDTO;

public interface UserService {

	public UserDTO whoAmI(String tokenValue);
	
}
