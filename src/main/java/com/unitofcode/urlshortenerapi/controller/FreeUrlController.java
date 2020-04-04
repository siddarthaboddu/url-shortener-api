package com.unitofcode.urlshortenerapi.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unitofcode.urlshortenerapi.dto.Status;
import com.unitofcode.urlshortenerapi.dto.UrlPasswordValidationRequest;
import com.unitofcode.urlshortenerapi.model.Url;
import com.unitofcode.urlshortenerapi.service.UrlService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/free/api")
@Slf4j
public class FreeUrlController {

	@Autowired
	private UrlService urlService;

	@GetMapping("/longUrl/{shortCode}")
	public ResponseEntity<Url> getLongUrl(HttpServletRequest httpServletRequest, @PathVariable("shortCode") String shortUrl){
		Url url = urlService.getLongUrl(httpServletRequest, shortUrl);
		
		if(url == null) {
			return new ResponseEntity<Url>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Url>(url, HttpStatus.OK);
	}
	
	
	@PostMapping("/url/validPassword")
	public ResponseEntity<Status> isValidPasswordForUrl(@RequestBody UrlPasswordValidationRequest urlPasswordValidationRequest){
		Optional<Status> status = urlService.isValidUrlAndPassword(urlPasswordValidationRequest);
		
		if(status.isPresent()) {
			return new ResponseEntity<>(status.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
