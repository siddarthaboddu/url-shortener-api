package com.unitofcode.urlshortenerapi.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unitofcode.urlshortenerapi.dto.Status;
import com.unitofcode.urlshortenerapi.dto.UrlPasswordValidationRequest;
import com.unitofcode.urlshortenerapi.dto.VisitResponse;
import com.unitofcode.urlshortenerapi.model.Url;
import com.unitofcode.urlshortenerapi.service.UrlService;
import com.unitofcode.urlshortenerapi.service.VisitService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class UrlController {

	@Autowired
	private UrlService urlService;

	@Autowired
	private VisitService visitService;
	
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
	
	
	@GetMapping("/url/history/last30Days/all")
	public ResponseEntity<List<VisitResponse>> getLastOneMonthResponse(HttpServletRequest httpServletRequest){
		Optional<List<VisitResponse>> visits = visitService.getLast30DaysHistory(httpServletRequest);
		
		if(visits.isPresent()) {
			return new ResponseEntity<>(visits.get(), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/url/history/last1Year/all")
	public ResponseEntity<List<VisitResponse>> getlastOneYearResponse(HttpServletRequest httpServletRequest){
		Optional<List<VisitResponse>> visits = visitService.getlastOneYearResponse(httpServletRequest);
		
		if(visits.isPresent()) {
			return new ResponseEntity<>(visits.get(), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/url/history/last30Days")
	public ResponseEntity<List<VisitResponse>> getLastOneMonthVisitForUrl(HttpServletRequest httpServletRequest, @RequestParam("urlId") Long urlId){
		Optional<List<VisitResponse>> visits = visitService.getLast30DaysHistory(httpServletRequest, urlId);
		
		if(visits.isPresent()) {
			return new ResponseEntity<>(visits.get(), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/url/history/last1Year")
	public ResponseEntity<List<VisitResponse>> getlastOneYearResponseForUrl(HttpServletRequest httpServletRequest, @RequestParam("urlId") Long urlId){
		Optional<List<VisitResponse>> visits = visitService.getlastOneYearResponse(httpServletRequest, urlId);
		
		if(visits.isPresent()) {
			return new ResponseEntity<>(visits.get(), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	

	
	
}
