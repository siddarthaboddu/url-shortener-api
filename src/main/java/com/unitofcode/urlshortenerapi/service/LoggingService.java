package com.unitofcode.urlshortenerapi.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoggingService {
	
	public void logRequest(HttpServletRequest request) {
		log.info("request_log: {}",request);
	}
	
	public void logResponse(HttpServletResponse response) {
		log.info("response_log: {}",response);
	}

}
