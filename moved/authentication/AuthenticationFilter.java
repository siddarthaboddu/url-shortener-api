package com.unitofcode.urlshortenerapi.authentication;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static final String AUTHORIZATION = "Authorization";
	
	public AuthenticationFilter(final RequestMatcher requiresAuth){
		super(requiresAuth);
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		
		String token = request.getHeader(AUTHORIZATION);
		token = StringUtils.removeStart(token, "Bearer").trim();
		Authentication requestAuthentication = new UsernamePasswordAuthenticationToken(token, token);
		return getAuthenticationManager().authenticate(requestAuthentication);
	
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		SecurityContextHolder.getContext().setAuthentication(authResult);
		chain.doFilter(request, response);
	}

}
