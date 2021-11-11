package com.spring.api.global.util.file;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CropRequest {

	private int left;
	private int top;
	private int width;
	private int height;
	private final int size;
	
	public CropRequest(int size) {
		this.size = size;
	}

	public CropRequest(int left, int top, int width, int height, int size) {
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
		this.size = size;
	}
	
}
