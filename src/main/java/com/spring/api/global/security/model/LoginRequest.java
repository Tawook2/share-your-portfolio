package com.spring.api.global.security.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginRequest {

	private String username;
	private String password;
	
}
