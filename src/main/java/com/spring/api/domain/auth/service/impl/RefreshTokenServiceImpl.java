package com.spring.api.domain.auth.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.spring.api.domain.auth.exception.InvalidRefreshTokenException;
import com.spring.api.domain.auth.exception.NotFoundRefreshTokenException;
import com.spring.api.domain.auth.mapper.RefreshTokenMapper;
import com.spring.api.domain.auth.service.RefreshTokenService;
import com.spring.api.domain.model.RefreshToken;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

	@Autowired
	private RefreshTokenMapper mapper;
	
	@Value("${refreshToken.validationTime}")
	private long validationTime;
	
	@Value("${refreshToken.cookieName}")
	private String cookieName;
	
	@Value("${refreshToken.cookiePath}")
	private String cookiePath;
	
	@Override
	public RefreshToken generateRefreshToken(int accountId) {
		String token = UUID.randomUUID().toString();
		RefreshToken refreshToken = RefreshToken.builder()
										.accountId(accountId)
										.token(token)
										.expiredDate(new Timestamp(System.currentTimeMillis() + validationTime))
										.build();
		
		mapper.save(refreshToken);
		
		return refreshToken;
	}

	@Override
	public Cookie createRefreshTokenCookie(RefreshToken refreshToken) {
		Cookie cookie = new Cookie(cookieName, refreshToken.getToken());
		cookie.setHttpOnly(true);
		cookie.setMaxAge((int) (validationTime / 1000));
		cookie.setPath(cookiePath);
		return cookie;
	}

	@Override
	public String getToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals(cookieName)) {
					return c.getValue();
				}
			}
		}
		
		throw new NotFoundRefreshTokenException();
	}

	@Override
	public void verify(String token, int id) {
		RefreshToken saved = mapper.findRefreshTokenByAccountId(id);
		if (saved == null
				|| !saved.getToken().equals(token)
				|| saved.getExpiredDate().before(new Date())) {
			throw new InvalidRefreshTokenException();
		}
	}

	@Override
	public void deleteRefreshToken(int accountId) {
		mapper.deleteRefreshTokenByAccountId(accountId);
	}

	@Override
	public void deleteRefreshTokenCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals(cookieName)) {
					c.setMaxAge(0);
					response.addCookie(c);
				}
			}
		}
	}

}
