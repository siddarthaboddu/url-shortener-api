package com.unitofcode.urlshortenerapi.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unitofcode.urlshortenerapi.model.Url;
import com.unitofcode.urlshortenerapi.model.Visit;
import com.unitofcode.urlshortenerapi.repository.UrlRepository;
import com.unitofcode.urlshortenerapi.repository.VisitRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AsyncService {
	
	@Autowired
	UrlRepository urlRepository;

	@Autowired
	VisitRepository visitRepository;
	
	public void incrementUrlVisits(HttpServletRequest httpServletRequest, Url url) {
		incrementUrl(url);
		addUrlVisitHistory(httpServletRequest, url);
	}
	
	private void incrementUrl(Url url) {
		url.setVisits(url.getVisits() + 1);
		urlRepository.save(url);
	}
	
	private void addUrlVisitHistory(HttpServletRequest httpServletRequest,Url url) {
		Visit visit = new Visit();
		
		visit.setUrlId(url.getId());
		
		ZonedDateTime gmt = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("GMT"));
		
		visit.setCreateTime(Timestamp.valueOf(gmt.toLocalDateTime()));
				
		visit.setReferer(httpServletRequest.getHeader("REFERER-UI"));
		visit.setUserId(url.getUser().getId());
		
		visitRepository.save(visit);
	}

}
