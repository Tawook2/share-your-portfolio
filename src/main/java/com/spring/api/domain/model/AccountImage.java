package com.spring.api.domain.model;

import org.apache.ibatis.type.Alias;

import lombok.Builder;
import lombok.Getter;

@Alias("AccountImage")
@Getter
public class AccountImage {
	
	private int accountId;
	private String path;
	
	@Builder
	public AccountImage(int accountId, String path) {
		this.accountId = accountId;
		this.path = path;
	}
	
}
