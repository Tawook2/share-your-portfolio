package com.spring.api.domain.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class NotFoundRefreshTokenException extends AuthenticationException {

	private static final String MESSAGE = "Refresh Token을 찾을 수 없습니다.";
	
	public NotFoundRefreshTokenException() {
		super(MESSAGE);
	}
	
}
