package com.unitofcode.urlshortenerapi.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unitofcode.urlshortenerapi.dto.RetreiveRequest;
import com.unitofcode.urlshortenerapi.dto.ShortenRequest;
import com.unitofcode.urlshortenerapi.model.Url;
import com.unitofcode.urlshortenerapi.model.User;
import com.unitofcode.urlshortenerapi.service.ShortenerService;
import com.unitofcode.urlshortenerapi.service.UrlService;
import com.unitofcode.urlshortenerapi.service.UserService;

@RestController
@RequestMapping("/api")
public class ShortenerController {

	@Autowired
	private ShortenerService shortenerService;
	
	@Autowired
	private UrlService urlService;
	
	@Autowired
	private UserService userService;

	@PostMapping("/shorten")
	public ResponseEntity<String> shortenLink(@RequestHeader(value = "access-token", required = false) String access_token,
			@RequestBody ShortenRequest request, HttpServletRequest httpServletRequest) {
		Optional<User> user = userService.getUserByAccessToken(access_token);
		if(shortenerService.isLimitReached(access_token, request, httpServletRequest, user)) {
			return new ResponseEntity<String>("maximum request limit reached, to get more request wait for 1 day or upgrade to premium account", HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
		}
		String shortenedUrl = shortenerService.shorten(request);
		return new ResponseEntity<String>(shortenedUrl, HttpStatus.CREATED);
	}

	@PostMapping("/retreive")
	public ResponseEntity<String> getOriginalLink(@RequestHeader(value = "access-token", required = false) String user_token,
			@RequestBody RetreiveRequest request) {
		String originalUrl = shortenerService.getOriginal(request);
		return new ResponseEntity<String>(originalUrl, HttpStatus.OK);
	}
	
	@GetMapping("/links")
	public ResponseEntity<List<Url>> getAllUrls(@RequestHeader(value = "access-token", required = false) String access_token,
			HttpServletRequest httpServletRequest){
		Optional<List<Url>> urls = urlService.getAllUrlsForUser(access_token, httpServletRequest);
		return new ResponseEntity<List<Url>>(urls.get(), HttpStatus.ACCEPTED);
	}

}
