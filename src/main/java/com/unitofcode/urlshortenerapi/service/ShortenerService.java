package com.unitofcode.urlshortenerapi.service;

import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.unitofcode.urlshortenerapi.dto.RetreiveRequest;
import com.unitofcode.urlshortenerapi.dto.ShortenRequest;
import com.unitofcode.urlshortenerapi.dto.UrlResponse;
import com.unitofcode.urlshortenerapi.model.ClientUsage;
import com.unitofcode.urlshortenerapi.model.Url;
import com.unitofcode.urlshortenerapi.model.User;
import com.unitofcode.urlshortenerapi.repository.ClientUsageRepository;
import com.unitofcode.urlshortenerapi.repository.UrlRepository;
import com.unitofcode.urlshortenerapi.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ShortenerService {

	@Autowired
	private UrlRepository urlRepository;

	@Autowired
	ClientUsageRepository clientUsageRepository;
	
	@Autowired
	UserRepository userRepository;

	@Value("${url.shortener.hostname}")
	String hostName;
	
	@Value("${url.shortener.client.prefix}")
	String clientPrefix;

	private Random random = new Random();

	private String characters = "abdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	private int shortUrlLength = 8;

	public UrlResponse shorten(ShortenRequest request, Optional<User> user, ClientUsage clientUsage) {

		String longUrl = request.getUrl();

		String randomString = "";
		do {
			randomString = generateRandomString();
		} while (dbContainsString(randomString));

		Url url = new Url();
		url.setShortUrl(randomString);
		url.setLongUrl(longUrl);
		if(user.isPresent())
			url.setUser(user.get());
		urlRepository.save(url);

		String shortUrl = clientPrefix + randomString;
		UrlResponse urlResponse = new UrlResponse();
		urlResponse.setShortUrl(shortUrl);
		urlResponse.setOriginalUrl(longUrl);
		urlResponse.setUsageLeft(clientUsage.getUsageLeft());
		return urlResponse;
	}

	private String generateRandomString() {
		StringBuilder randomString = new StringBuilder("");

		for (int i = 0; i < shortUrlLength; i++) {
			randomString.append(characters.charAt(random.nextInt(61)));
		}
		return randomString.toString();
	}

	private boolean dbContainsString(String randomString) {
		Url url = urlRepository.findFirstByShortUrl(randomString);
		if (url != null)
			return true;
		else
			return false;
	}

	public String getOriginal(RetreiveRequest request) {
		Url url = urlRepository.findFirstByShortUrl(request.getUrl());
		if(url == null) throw new RuntimeException("Not Found");
		return url.getLongUrl();
	}

	
	public ClientUsage usageLeft(ShortenRequest shortenRequest,
			HttpServletRequest httpServletRequest, Optional<User> optionalUser, String Tier) {
		ClientUsage clientUsage = null;
		String synchronizedObject = "";
		if(optionalUser.isPresent()) {
			synchronizedObject=""+optionalUser.get().getId();
		}
		else {
			synchronizedObject=""+httpServletRequest.getRemoteAddr();
		}
		synchronized(synchronizedObject.intern()) {
			if (optionalUser.isPresent()) {
				User user = optionalUser.get();
				clientUsage = clientUsageRepository.findFirstByUserId(user.getId());
				if (clientUsage == null) {
					clientUsage = new ClientUsage();
					clientUsage.setUserId(user.getId());
					
					if(Tier.equalsIgnoreCase("FREE"))
						clientUsage.setUsageLeft(200l);
					if(Tier.equalsIgnoreCase("USER"))
						clientUsage.setUsageLeft(2000l);
					
				}
			} else {
				String ipAddress = httpServletRequest.getRemoteAddr();

				clientUsage = clientUsageRepository.findFirstByIpAddress(ipAddress);

				if (clientUsage == null) {
					clientUsage = new ClientUsage();
					clientUsage.setIpAddress(ipAddress);
					
					if(Tier.equalsIgnoreCase("FREE"))
						clientUsage.setUsageLeft(200l);
					if(Tier.equalsIgnoreCase("USER"))
						clientUsage.setUsageLeft(2000l);
					
				}
			}


			
			clientUsage.setUsageLeft(clientUsage.getUsageLeft() - 1);
			clientUsage = clientUsageRepository.save(clientUsage);
			log.info("clientUsage left : {}", clientUsage.getUsageLeft());
			return clientUsage;
		}
		
	}

}
