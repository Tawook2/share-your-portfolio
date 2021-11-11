package com.spring.api.domain.auth.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

public interface JwtService {
	
	public String generateToken(int id, String username);
	
	public String getToken(HttpServletRequest request);
	
	public void verify(String token) throws TokenExpiredException, JWTVerificationException;
	
	public int getId(String token);
	
	public String getUsername(String token);
	
	public Authentication getAuthentication(String token);
	
}
