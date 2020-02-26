package com.unitofcode.urlshortenerapi.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoggingService {
	
	private ObjectMapper objMapper;
	
	public LoggingService() {
		objMapper = new ObjectMapper();
	}
	public void logRequest(HttpServletRequest request) throws JsonProcessingException {
//		log.info("request_log: {}",objMapper.writeValueAsString(request));
	}
	
	public void logResponse(HttpServletResponse response) throws JsonProcessingException {
//		log.info("response_log: {}",objMapper.writeValueAsBytes(response));
	}

}
