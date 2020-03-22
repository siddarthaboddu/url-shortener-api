package com.unitofcode.urlshortenerapi.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unitofcode.urlshortenerapi.model.Url;
import com.unitofcode.urlshortenerapi.service.UrlService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class UrlController {

	@Autowired
	private UrlService urlService;

	@GetMapping("/urls")
	public ResponseEntity<List<Url>> getAllUrls(HttpServletRequest httpServletRequest) {
		Optional<List<Url>> urls = urlService.getAllUrlsForUser(httpServletRequest);
		if (urls.isPresent())
			return new ResponseEntity<List<Url>>(urls.get(), HttpStatus.OK);
		else
			return new ResponseEntity<List<Url>>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/url/{urlId}")
	public ResponseEntity<Url> getUrl(HttpServletRequest httpServletRequest, @PathVariable("urlId") Long urlId ){
		Optional<Url> url = urlService.getUrlDetails(urlId, httpServletRequest);
		if(url.isPresent()) {
			return new ResponseEntity<Url>(url.get(), HttpStatus.OK);
		}
		return  new ResponseEntity<Url>(HttpStatus.NO_CONTENT);
	}
	
}
