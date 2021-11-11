package com.spring.api.domain.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.spring.api.domain.auth.service.JwtService;
import com.spring.api.domain.auth.service.RefreshTokenService;
import com.spring.api.global.error.model.ErrorResponse;
import com.spring.api.global.security.model.LoginResponse;

import lombok.extern.log4j.Log4j;

@Log4j
@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@GetMapping("/refresh")
	public ResponseEntity<LoginResponse> getRefreshToken(HttpServletRequest request) {
		log.info("refresh token!");
		String accessToken = jwtService.getToken(request);
		String refreshToken = refreshTokenService.getToken(request);
		
		// Access Token 유효성 검사
		try {
			jwtService.verify(accessToken);
		} catch (TokenExpiredException e) {
			// 토큰이 만료된 경우도 계속 진행함.
		} 
		
		int id = jwtService.getId(accessToken);
		String username = jwtService.getUsername(accessToken);
		
		// Refresh Token 유효성 검사
		refreshTokenService.verify(refreshToken, id);
		
		// 새로운 Access Token 생성
		String newAccessToken = jwtService.generateToken(id, username);
		LoginResponse response = LoginResponse.builder()
										.token(newAccessToken)
										.build();
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
		log.info("logout!");
		refreshTokenService.deleteRefreshTokenCookie(request, response);
		
		String token = jwtService.getToken(request);
		int id = jwtService.getId(token);
		refreshTokenService.deleteRefreshToken(id);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@ExceptionHandler({Exception.class})
	protected ResponseEntity<ErrorResponse> handleException(Exception e) {
		ErrorResponse response = ErrorResponse.builder()
									.httpStatus(HttpStatus.UNAUTHORIZED)
									.message(e.getMessage())
									.build();
		
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
	}
	
}
