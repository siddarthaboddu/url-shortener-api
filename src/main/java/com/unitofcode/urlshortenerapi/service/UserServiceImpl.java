package com.unitofcode.urlshortenerapi.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.unitofcode.urlshortenerapi.constants.Constants;
import com.unitofcode.urlshortenerapi.dto.AccessTokenRequest;
import com.unitofcode.urlshortenerapi.dto.AccessTokenResponse;
import com.unitofcode.urlshortenerapi.dto.UserRequest;
import com.unitofcode.urlshortenerapi.model.User;
import com.unitofcode.urlshortenerapi.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public boolean isValidLogin(String email, String rawPassword) {
		Optional<User> optionalUser = userRepository.findFirstByEmail(email);
		
		if (optionalUser.isPresent() && bCryptPasswordEncoder.matches(rawPassword, optionalUser.get().getPasswordHash())) {
			return true;
		}
		return false;
	}

	@Override
	public AccessTokenResponse generateAccessToken(AccessTokenRequest accessTokenRequest) {
		AccessTokenResponse accessTokenResponse = new AccessTokenResponse();
		if(isValidLogin(accessTokenRequest.getEmail(), accessTokenRequest.getPassword())) {
			List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
			
			String token = Jwts.builder()
					.setId(UUID.randomUUID().toString())
					.setSubject(accessTokenRequest.getEmail())
					.claim("authorities",
							grantedAuthorities.stream()
									.map(GrantedAuthority::getAuthority)
									.collect(Collectors.toList()))
					.setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + 6000000))
					.signWith(SignatureAlgorithm.HS512, Constants.SECRET_KEY.getBytes()).compact();
			
			accessTokenResponse.setAccessToken(token);
			accessTokenResponse.setTokenType(Constants.PREFIX.trim());
			accessTokenResponse.setExpiresIn(600);
		}
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

	@Override
	public Optional<User> getUserByAccessToken(String access_token) {
		Optional<User> user = userRepository.findFirstByAccessToken(access_token);
		return user;
	}
	
	@Override
	public User getCurrentUser(HttpServletRequest request) {
		Optional<User> user = userRepository.findFirstByEmail(request.getAttribute(Constants.EMAIL_ADDRESS.toString()).toString());
		if(user.isPresent())
			return user.get();
		return null;
	}


	
	
}
