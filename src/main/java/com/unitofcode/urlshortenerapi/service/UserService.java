package com.unitofcode.urlshortenerapi.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.unitofcode.urlshortenerapi.dto.AccessTokenRequest;
import com.unitofcode.urlshortenerapi.dto.AccessTokenResponse;
import com.unitofcode.urlshortenerapi.dto.UserRequest;
import com.unitofcode.urlshortenerapi.model.User;

public interface UserService {

	public boolean isValidLogin(String email, String password);
		
	public AccessTokenResponse generateAccessToken(AccessTokenRequest accessTokenRequest);
	
	public User createUser(UserRequest user);
	
	public User getCurrentUser(HttpServletRequest request);
	
	public Optional<User> getUserByAccessToken(String access_token);
		
}
