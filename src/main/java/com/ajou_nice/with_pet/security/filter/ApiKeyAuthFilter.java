package com.ajou_nice.with_pet.security.filter;

import com.ajou_nice.with_pet.security.handler.ApiKeyAuthManager;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyAuthFilter extends AbstractPreAuthenticatedProcessingFilter {
	private String externalKey = "Authentication";
	@Autowired
	public ApiKeyAuthFilter(ApiKeyAuthManager manager){
		this.setAuthenticationManager(manager);
	}
	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request){
		return request.getHeader(externalKey);
	}
	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request){
		return "N/A";
	}

}
