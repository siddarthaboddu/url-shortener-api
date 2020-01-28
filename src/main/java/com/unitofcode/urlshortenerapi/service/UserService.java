package com.unitofcode.urlshortenerapi.service;

import java.util.Optional;

import com.unitofcode.urlshortenerapi.dto.AccessTokenRequest;
import com.unitofcode.urlshortenerapi.dto.AccessTokenResponse;
import com.unitofcode.urlshortenerapi.dto.UserRequest;
import com.unitofcode.urlshortenerapi.model.User;

public interface UserService {

	public String login(String email, String password);
	
	public Optional<org.springframework.security.core.userdetails.User> findByToken(String token);
	
	public AccessTokenResponse generateAccessToken(AccessTokenRequest accessTokenRequest);
	
	public User createUser(UserRequest user);
		
}
