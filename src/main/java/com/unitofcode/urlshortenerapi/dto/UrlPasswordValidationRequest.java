package com.unitofcode.urlshortenerapi.dto;

import lombok.Data;

@Data
public class UrlPasswordValidationRequest {

	private String shortUrl;
	private String password;
	
}
