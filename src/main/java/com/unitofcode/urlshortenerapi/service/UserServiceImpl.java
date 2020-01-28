package com.unitofcode.urlshortenerapi.service;

import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.unitofcode.urlshortenerapi.dto.AccessTokenRequest;
import com.unitofcode.urlshortenerapi.dto.AccessTokenResponse;
import com.unitofcode.urlshortenerapi.dto.UserRequest;
import com.unitofcode.urlshortenerapi.model.User;
import com.unitofcode.urlshortenerapi.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public String login(String email, String rawPassword) {
		Optional<User> optionalUser = userRepository.findFirstByEmail(email);
		
		if (optionalUser.isPresent() && bCryptPasswordEncoder.matches(rawPassword, optionalUser.get().getPasswordHash())) {
			User user = optionalUser.get();
			String newAccessToken = generateAccessToken();
			user.setAccessToken(newAccessToken);
			userRepository.save(user);
			return newAccessToken;
		}
		return StringUtils.EMPTY;
	}

	@Override
	public Optional<org.springframework.security.core.userdetails.User> findByToken(String token) {
		Optional<User> optionalUser = userRepository.findFirstByAccessToken(token);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswordHash(), true, true, true, true,
					AuthorityUtils.createAuthorityList("USER"));
			return Optional.of(userDetails);
		}
		return Optional.empty();
	}

	private String generateAccessToken() {
		return RandomStringUtils.random(150, true, true);
	}

	@Override
	public AccessTokenResponse generateAccessToken(AccessTokenRequest accessTokenRequest) {
		AccessTokenResponse accessTokenResponse = new AccessTokenResponse();
		accessTokenResponse.setAccessToken(login(accessTokenRequest.getEmail(), accessTokenRequest.getPassword()));
		return accessTokenResponse;
	}
	
	@Override
	public User createUser(UserRequest userRequest) {
		String passwordHash = bCryptPasswordEncoder.encode(userRequest.getPassword());
		
		User user = new User();
		user.setEmail(userRequest.getEmail());
		user.setFirstName(userRequest.getFirstName());
		user.setLastName(userRequest.getLastName());
		user.setPasswordHash(passwordHash);
		log.info("before user : {}",user);
		user = userRepository.save(user);
		log.info("after user : {}",user);
		return user;
	}


	
	
}
