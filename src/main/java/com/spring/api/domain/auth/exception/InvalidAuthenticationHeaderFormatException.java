package com.spring.api.domain.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidAuthenticationHeaderFormatException extends AuthenticationException {

	private static final String MESSAGE = "잘못된 헤더 포맷 입니다.";
	
	public InvalidAuthenticationHeaderFormatException() {
		super(MESSAGE);
	}
	
}
