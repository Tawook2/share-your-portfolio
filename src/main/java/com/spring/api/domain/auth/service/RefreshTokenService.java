package com.spring.api.domain.auth.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.spring.api.domain.model.RefreshToken;

public interface RefreshTokenService {
	
	public RefreshToken generateRefreshToken(int accountId);
	
	public Cookie createRefreshTokenCookie(RefreshToken refreshToken);

	public String getToken(HttpServletRequest request);

	public void verify(String refreshToken, int id);
	
	public void deleteRefreshToken(int accountId);
	
	public void deleteRefreshTokenCookie(HttpServletRequest request, HttpServletResponse response);
	
}
