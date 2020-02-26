package com.unitofcode.urlshortenerapi.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.unitofcode.urlshortenerapi.dto.AccessTokenRequest;
import com.unitofcode.urlshortenerapi.dto.AccessTokenResponse;
import com.unitofcode.urlshortenerapi.dto.RegisterResponse;
import com.unitofcode.urlshortenerapi.dto.UserRequest;
import com.unitofcode.urlshortenerapi.model.User;
import com.unitofcode.urlshortenerapi.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserController {

	@Autowired
	UserService userService;
	
	@PostMapping("/token/login")
	public ResponseEntity<AccessTokenResponse> generateAccessToken(@RequestBody AccessTokenRequest accessTokenRequest){
		AccessTokenResponse accessTokenResponse = userService.generateAccessToken(accessTokenRequest);
		if(StringUtils.isEmpty(accessTokenResponse.getAccessToken()))
			return new ResponseEntity<>(accessTokenResponse,HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(accessTokenResponse,HttpStatus.CREATED);
	}
	
	@PostMapping("/token/register")
	public ResponseEntity<RegisterResponse> createUser(@RequestBody UserRequest userRequest){
		
		try {
			log.info("userRequest : {}",userRequest);
			User user = userService.createUser(userRequest);
			if(user == null) throw new RuntimeException("");
			RegisterResponse registerResponse = new RegisterResponse();
			registerResponse.setStatus("true");
			registerResponse.setUser(user);
			return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
		}
		catch(Exception e) {
			RegisterResponse registerResponse = new RegisterResponse();
			registerResponse.setStatus("false");
			return new ResponseEntity<>(registerResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
}
