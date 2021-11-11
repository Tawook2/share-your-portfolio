package com.spring.api.domain.auth.mapper;

import com.spring.api.domain.model.RefreshToken;

public interface RefreshTokenMapper {
	
	public void save(RefreshToken refreshToken);

	public RefreshToken findRefreshTokenByAccountId(int accountId);
	
	public void deleteRefreshTokenByAccountId(int accountId);
	
}
