package com.unitofcode.urlshortenerapi.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.unitofcode.urlshortenerapi.constants.Constants;
import com.unitofcode.urlshortenerapi.dto.Status;
import com.unitofcode.urlshortenerapi.dto.UrlPasswordValidationRequest;
import com.unitofcode.urlshortenerapi.model.Url;
import com.unitofcode.urlshortenerapi.model.User;
import com.unitofcode.urlshortenerapi.repository.UrlRepository;
import com.unitofcode.urlshortenerapi.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UrlService {

	@Autowired
	UrlRepository urlRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AsyncService asyncService;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public Optional<List<Url>> getAllUrlsForUser(HttpServletRequest httpServletRequest) {
		Optional<User> currentUser = userRepository.findFirstByEmail(httpServletRequest.getAttribute(Constants.EMAIL_ADDRESS).toString());
		if(currentUser.isPresent()) {
			List<Url> urls = urlRepository.findAllByUserId(currentUser.get().getId());
			if(urls.size() >= 0)
				return Optional.of(urls);
			else
				return Optional.empty();
		}
		return Optional.empty();
	}

	public Optional<Url> getUrlDetails(Long urlId, HttpServletRequest httpServletRequest){
		Optional<User> currentUser = userRepository.findFirstByEmail(httpServletRequest.getAttribute(Constants.EMAIL_ADDRESS).toString());
		if(currentUser.isPresent()) {
			Optional<Url> url = urlRepository.findById(urlId);
			if(url.isPresent())
				return url;
			else
				return Optional.empty();
		}
		return Optional.empty();
	}

	public Url getLongUrl(HttpServletRequest httpServletRequest, String shortUrl) {
		Url url = urlRepository.findFirstByShortUrl(shortUrl);
		if(url == null) {
			return null;
		}
		
		asyncService.incrementUrlVisits(httpServletRequest, url);

		Url responseUrl = new Url();
		responseUrl.setLongUrl(url.getLongUrl());
		responseUrl.setShortUrl(url.getShortUrl());
		responseUrl.setVisits(url.getVisits());
		responseUrl.setPasswordHash(url.getPasswordHash());
		return responseUrl;
	}

	public Optional<Status> isValidUrlAndPassword(UrlPasswordValidationRequest urlRequest) {
		log.info("url request, {}", urlRequest);
		Url url = urlRepository.findFirstByShortUrl(urlRequest.getShortUrl());
		log.info("url retrieve {}",url);
		if(url == null) {
			return Optional.empty();
		}
			
		String requestPassword = urlRequest.getPassword();
		String urlPasswordHash = url.getPasswordHash();
		
		if(bCryptPasswordEncoder.matches(requestPassword, urlPasswordHash)) {
			return Optional.of(new Status(true));
		}
		else {
			return Optional.of(new Status(false));
		}
	}
	
}
