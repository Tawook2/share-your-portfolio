package com.spring.api.domain.auth.service.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.spring.api.domain.auth.exception.InvalidAuthenticationHeaderFormatException;
import com.spring.api.domain.auth.exception.NotFoundAuthenticationHeaderException;
import com.spring.api.domain.auth.service.JwtService;
import com.spring.api.global.security.model.LoginDetails;

@Service
public class JwtServiceImpl implements JwtService {

	private static final String CLAIM_ID = "id";
	private static final String CLAIM_USERNAME = "username";
	
	@Value("${jwt.header}")
	private String headerName;
	
	@Value("${jwt.tokenPrefix}")
	private String tokenPrefix;
	
	@Value("${jwt.validationTime}")
	private long validationTime;
	
	@Value("${jwt.secretKey}")
	private String secretKey;
	
	@Override
	public String generateToken(int id, String username) {
		return tokenPrefix + JWT.create()
								.withClaim(CLAIM_ID, id)
								.withClaim(CLAIM_USERNAME, username)
								.withExpiresAt(new Date(System.currentTimeMillis() + validationTime))
								.sign(Algorithm.HMAC256(secretKey));
	}

	@Override
	public String getToken(HttpServletRequest request) {
		String headerContent = request.getHeader(headerName);
		if (headerContent == null) {
			throw new NotFoundAuthenticationHeaderException();
		}
		
		if (!headerContent.startsWith(tokenPrefix)) {
			throw new InvalidAuthenticationHeaderFormatException();
		}
		
		return headerContent.replace(tokenPrefix, "");
	}

	@Override
	public void verify(String token) throws TokenExpiredException, JWTVerificationException {
		JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
	}

	@Override
	public int getId(String token) {
		return JWT.decode(token).getClaim(CLAIM_ID).asInt();
	}

	@Override
	public String getUsername(String token) {
		return JWT.decode(token).getClaim(CLAIM_USERNAME).asString();
	}
	
	@Override
	public Authentication getAuthentication(String token) {
		int id = getId(token);
		String username = getUsername(token);
		LoginDetails loginDetails = new LoginDetails(id, username);
		
		return new UsernamePasswordAuthenticationToken(loginDetails, null, loginDetails.getAuthorities());
	}
	
}
