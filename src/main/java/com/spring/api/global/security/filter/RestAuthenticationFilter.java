package com.spring.api.global.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.api.domain.auth.service.JwtService;
import com.spring.api.domain.auth.service.RefreshTokenService;
import com.spring.api.domain.model.RefreshToken;
import com.spring.api.global.error.model.ErrorResponse;
import com.spring.api.global.security.exception.AuthenticationIOException;
import com.spring.api.global.security.model.LoginDetails;
import com.spring.api.global.security.model.LoginRequest;
import com.spring.api.global.security.model.LoginResponse;

import lombok.extern.log4j.Log4j;

@Log4j
public class RestAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private static final String RESPONSE_CONTENT_TYPE = "application/json;charset=utf-8";
	
	private static final String BAD_REQUEST_MESSAGE = "잘못된 요청 정보";
	private static final String LOGIN_FAIL_MESSAGE = "입력한 정보와 일치하는 회원정보 없음.";
	private static final String UNKNOWN_ERROR_MESSAGE = "알 수 없는 에러 발생";
	
	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private JwtService jwtService;

	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		log.info("attempt login...");
		
		LoginRequest loginRequest = null;
		try {
			loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
		} catch (IOException e) {
			log.warn(e);	
			throw new AuthenticationIOException();
		}
	
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
		return getAuthenticationManager().authenticate(authToken);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		log.info("login success!");
		
		LoginDetails user = (LoginDetails) authResult.getPrincipal();
		String accessToken = jwtService.generateToken(user.getId(), user.getUsername());
		RefreshToken refreshToken = refreshTokenService.generateRefreshToken(user.getId());
		
		LoginResponse loginResponse = LoginResponse.builder()
											.token(accessToken)
											.build();

		response.setContentType(RESPONSE_CONTENT_TYPE);
		response.getWriter().println(objectMapper.writeValueAsString(loginResponse));
		response.addCookie(refreshTokenService.createRefreshTokenCookie(refreshToken));
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		log.info("login fail!");
		log.info(failed);
		
		HttpStatus httpStatus = null;
		String message = null;
		if (failed instanceof AuthenticationIOException) {
			httpStatus = HttpStatus.BAD_REQUEST;
			message = BAD_REQUEST_MESSAGE;
		} else if (failed instanceof BadCredentialsException) {
			httpStatus = HttpStatus.UNAUTHORIZED;
			message = LOGIN_FAIL_MESSAGE;
		} else {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			message = UNKNOWN_ERROR_MESSAGE;
		}
		
		ErrorResponse errorResponse = ErrorResponse.builder()
											.httpStatus(httpStatus)
											.message(message)
											.build();
		
		response.setContentType(RESPONSE_CONTENT_TYPE);
		response.setStatus(errorResponse.getStatusCode());
		response.getWriter().print(objectMapper.writeValueAsString(errorResponse));
	}
	
}
