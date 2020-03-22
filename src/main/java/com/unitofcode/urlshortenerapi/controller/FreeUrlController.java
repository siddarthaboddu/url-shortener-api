package com.unitofcode.urlshortenerapi.controller;

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
}
