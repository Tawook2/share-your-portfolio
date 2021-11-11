package com.spring.api.domain.account.mapper;

import com.spring.api.domain.model.AccountImage;

public interface AccountImageMapper {

	public String findAccountImagePath(int accountId);
	
	public void save(AccountImage accountImage);
	
	public void delete(int accountId);
	
}
