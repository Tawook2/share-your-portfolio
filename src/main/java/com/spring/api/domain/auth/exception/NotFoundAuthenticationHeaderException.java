package com.spring.api.domain.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class NotFoundAuthenticationHeaderException extends AuthenticationException {

	private static final String MESSAGE = "Header 정보를 찾을 수 없습니다.";
	
	public NotFoundAuthenticationHeaderException() {
		super(MESSAGE);
	}
	
}
