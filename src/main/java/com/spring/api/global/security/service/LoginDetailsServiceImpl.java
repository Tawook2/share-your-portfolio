package com.spring.api.global.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.api.domain.account.mapper.AccountFindMapper;
import com.spring.api.domain.model.Account;
import com.spring.api.global.security.model.LoginDetails;

@Service
public class LoginDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AccountFindMapper mapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = mapper.findAccountByName(username);
		if (account == null) {
			throw new UsernameNotFoundException("Not Found - " + username);
		}
		
		return new LoginDetails(account);
	}

}
