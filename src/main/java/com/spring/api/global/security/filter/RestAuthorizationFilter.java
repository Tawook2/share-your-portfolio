package com.spring.api.global.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.spring.api.domain.auth.service.JwtService;

import lombok.extern.log4j.Log4j;

@Log4j
public class RestAuthorizationFilter extends BasicAuthenticationFilter {

	@Autowired
	private JwtService jwtService;
	
	public RestAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			String accessToken = jwtService.getToken(request);
			jwtService.verify(accessToken);
			
			Authentication auth = jwtService.getAuthentication(accessToken);
			SecurityContextHolder.getContext().setAuthentication(auth);
		} catch (Exception e) {
			log.info(e);
		}
		
		chain.doFilter(request, response);
		
	}
	
}
