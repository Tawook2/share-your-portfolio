package com.spring.api.domain.account.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.api.domain.account.mapper.AccountFindMapper;
import com.spring.api.domain.account.service.AccountFindService;
import com.spring.api.domain.model.Account;

@Service
public class AccountFindServiceImpl implements AccountFindService {
	
	@Autowired
	AccountFindMapper mapper;

	@Override
	public Account findAccountById(int id) {
		return mapper.findAccountById(id);
	}

	@Override
	public Account findAccountByName(String name) {
		return mapper.findAccountByName(name);
	}
	
}
