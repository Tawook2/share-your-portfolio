package com.spring.api.domain.model;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Alias("RefreshToken")
@Getter
@ToString
public class RefreshToken {
	
	private int accountId;
	private String token;
	private Timestamp expiredDate;
	
	@Builder
	public RefreshToken(int accountId, String token, Timestamp expiredDate) {
		this.accountId = accountId;
		this.token = token;
		this.expiredDate = expiredDate;
	}
	
}
