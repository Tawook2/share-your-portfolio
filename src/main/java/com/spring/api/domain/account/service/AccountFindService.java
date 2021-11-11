package com.spring.api.domain.account.service;

import com.spring.api.domain.model.Account;

public interface AccountFindService {

	public Account findAccountByName(String name);
	
	public Account findAccountById(int id);
	
}
