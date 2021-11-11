package com.spring.api.global.security.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

	private String token;
	
	@Builder
	public LoginResponse(String token) {
		this.token = token;
	}
	
}
