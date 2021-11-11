package com.spring.api.global.security.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.api.global.error.model.ErrorResponse;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private static final String RESPONSE_CONTENT_TYPE = "application/json;charset=utf-8";
	private static final String MESSAGE = "인증 실패";
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		ErrorResponse errorResponse = ErrorResponse.builder()
				.httpStatus(HttpStatus.UNAUTHORIZED)
				.message(MESSAGE)
				.build();

		response.setContentType(RESPONSE_CONTENT_TYPE);
		response.setStatus(errorResponse.getStatusCode());
		response.getWriter().print(objectMapper.writeValueAsString(errorResponse));
	}

}
