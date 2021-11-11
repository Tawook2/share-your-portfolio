package com.spring.api.global.security.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationIOException extends AuthenticationException {

	private static final String MESSAGE = "입력한 인증 정보를 처리하는 과정 중 문제가 발생하였습니다.";
	
	public AuthenticationIOException() {
		super(MESSAGE);
	}

	public AuthenticationIOException(String msg) {
		super(msg);
	}
	
}
