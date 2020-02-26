package com.unitofcode.urlshortenerapi.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unitofcode.urlshortenerapi.dto.RetreiveRequest;
import com.unitofcode.urlshortenerapi.dto.ShortenRequest;
import com.unitofcode.urlshortenerapi.dto.UrlResponse;
import com.unitofcode.urlshortenerapi.model.ClientUsage;
import com.unitofcode.urlshortenerapi.model.User;
import com.unitofcode.urlshortenerapi.service.ShortenerService;

@RestController
@RequestMapping("/free/api")
public class FreeShortenerController {

	@Autowired
	private ShortenerService shortenerService;

	@PostMapping("/shorten")
	public ResponseEntity<UrlResponse> shortenLink(@RequestBody ShortenRequest request,
			HttpServletRequest httpServletRequest) {
		Optional<User> user = Optional.empty();

		ClientUsage clientUsage = shortenerService.usageLeft(request, httpServletRequest, user, "FREE");

		if (clientUsage.getUsageLeft() < 0) {
			return new ResponseEntity<UrlResponse>(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
		}

		UrlResponse shortenedUrl = shortenerService.shorten(request, user, clientUsage);
		return new ResponseEntity<UrlResponse>(shortenedUrl, HttpStatus.CREATED);
	}

	@PostMapping("/retreive")
	public ResponseEntity<UrlResponse> getOriginalLink(@RequestBody RetreiveRequest request) {
		try {
			String originalUrl = shortenerService.getOriginal(request);
			UrlResponse urlResponse = new UrlResponse();
			urlResponse.setOriginalUrl(originalUrl);
			urlResponse.setShortUrl(request.getUrl());
			return new ResponseEntity<UrlResponse>(urlResponse, HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

}
