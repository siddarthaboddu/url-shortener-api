package com.unitofcode.urlshortenerapi.service;

import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.unitofcode.urlshortenerapi.dto.RetreiveRequest;
import com.unitofcode.urlshortenerapi.dto.ShortenRequest;
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
	String hostname;

	private Random random = new Random();

	private String characters = "abdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	private int shortUrlLength = 8;

	public String shorten(ShortenRequest request) {

		String longUrl = request.getUrl();

		String randomString = "";
		do {
			randomString = generateRandomString();
		} while (dbContainsString(randomString));

		Url url = new Url();
		url.setShortUrl(randomString);
		url.setLongUrl(longUrl);
		urlRepository.save(url);

		return hostname + randomString;
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
		return url.getLongUrl();
	}

	
	public boolean isLimitReached(String accessToken, ShortenRequest shortenRequest,
			HttpServletRequest httpServletRequest, Optional<User> optionalUser) {
		ClientUsage clientUsage = null;
		String synchronizedObject = "";
		if(optionalUser.isPresent()) {
			synchronizedObject=""+optionalUser.get().getId();
		}
		else {
			synchronizedObject=""+httpServletRequest.getRemoteAddr();
		}
		log.info("synchronized object : {}",synchronizedObject.intern());
		synchronized(synchronizedObject.intern()) {
			if (optionalUser.isPresent() && StringUtils.isNotBlank(accessToken)) {
				User user = optionalUser.get();
				clientUsage = clientUsageRepository.findFirstByUserId(user.getId());
				if (clientUsage == null) {
					clientUsage = new ClientUsage();
					clientUsage.setUserId(user.getId());
					clientUsage.setUsageLeft(200000l);
				}
			} else {
				String ipAddress = httpServletRequest.getRemoteAddr();
//			log.info(ipAddress);
				clientUsage = clientUsageRepository.findFirstByIpAddress(ipAddress);
//			log.info("value from redis db : {}",clientUsage);
				if (clientUsage == null) {
					clientUsage = new ClientUsage();
					clientUsage.setIpAddress(ipAddress);
					clientUsage.setUsageLeft(200000l);
				}
			}

			clientUsage.setUsageLeft(clientUsage.getUsageLeft() - 1);
			clientUsage = clientUsageRepository.save(clientUsage);
			log.info("clientUsage left : {}", clientUsage.getUsageLeft());
			if(clientUsage.getUsageLeft() < 0) {
				return true;
			}
			return false;
		}
		
	}

}
