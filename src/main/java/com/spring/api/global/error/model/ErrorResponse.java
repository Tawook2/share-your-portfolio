package com.spring.api.global.error.model;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

	private int statusCode;
	private String status;
	private String message;
	
	@Builder
	public ErrorResponse(HttpStatus httpStatus, String message) {
		this.statusCode = httpStatus.value();
		this.status = httpStatus.getReasonPhrase();
		this.message = message;
	}
	
}
