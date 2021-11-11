package com.spring.api.global.util.file;

import org.springframework.http.MediaType;

import lombok.Getter;

@Getter
public enum AccountImageMediaType {

	PNG(MediaType.IMAGE_PNG),
	JPG(MediaType.IMAGE_JPEG),
	JPGE(MediaType.IMAGE_JPEG);
	
	private MediaType mediaType;
	
	private AccountImageMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

}
