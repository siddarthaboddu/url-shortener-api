package com.unitofcode.urlshortenerapi.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unitofcode.urlshortenerapi.model.Url;
import com.unitofcode.urlshortenerapi.model.User;
import com.unitofcode.urlshortenerapi.repository.UrlRepository;
import com.unitofcode.urlshortenerapi.repository.UserRepository;

@Service
public class UrlService {

	@Autowired
	UrlRepository urlRepository;
	
	@Autowired
	UserRepository userRepository;
	
	public Optional<List<Url>> getAllUrlsForUser(String access_token, HttpServletRequest httpServletRequest) {
		Optional<User> currentUser = userRepository.findFirstByAccessToken(access_token);
		if(currentUser.isPresent()) {
			List<Url> urls = urlRepository.findAllByUserId(currentUser.get().getId());
			if(urls.size() > 0)
				return Optional.of(urls);
			else
				return Optional.empty();
		}
		return null;
	}

}
