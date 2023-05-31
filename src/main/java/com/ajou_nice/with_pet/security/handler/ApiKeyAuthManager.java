package com.ajou_nice.with_pet.security.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiKeyAuthManager implements AuthenticationManager {
	private final String apiKey = "forexternalapi";

	@Override
	public Authentication authenticate(Authentication authentication){
		String principal = (String) authentication.getPrincipal();
		if(!apiKey.equals(principal)){
			throw new BadCredentialsException("api key not found");
		}
		authentication.setAuthenticated(true);
		return authentication;
	}
}
