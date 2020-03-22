package com.unitofcode.urlshortenerapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unitofcode.urlshortenerapi.model.Url;
import com.unitofcode.urlshortenerapi.repository.UrlRepository;

@Service
public class AsyncService {
	
	@Autowired
	UrlRepository urlRepository;

	public void incrementUrlVisits(Url url) {
		// TODO Auto-generated method stub
		url.setVisits(url.getVisits()+1);
		urlRepository.save(url);
	}

}
