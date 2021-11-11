package com.spring.api.domain.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidRefreshTokenException extends AuthenticationException {

	private static final String MESSAGE = "유효하지 않는 토큰 입니다.";
	
	public InvalidRefreshTokenException() {
		super(MESSAGE);
	}

}
