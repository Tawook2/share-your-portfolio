package com.spring.api.domain.account.dto;

import javax.validation.constraints.Min;

import com.spring.api.global.util.file.CropRequest;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class AccountImageUploadRequest extends CropRequest {
	
	private static final int SIZE = 300;

	@Min(0)
	private int left;
	@Min(0)
	private int top;
	private int width;
	private int height;

	@Builder
	public AccountImageUploadRequest(int left, int top, int width, int height) {
		super(left, top, width, height, SIZE);
	}

	
}
