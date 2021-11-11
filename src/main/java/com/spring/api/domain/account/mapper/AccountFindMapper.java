package com.spring.api.domain.account.mapper;

import com.spring.api.domain.model.Account;

public interface AccountFindMapper {

	public Account findAccountByName(String name);
	
	public Account findAccountById(int id);
	
}
