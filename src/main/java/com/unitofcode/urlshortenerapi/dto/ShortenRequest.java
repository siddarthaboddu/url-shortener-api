package com.unitofcode.urlshortenerapi.dto;

import lombok.Data;

@Data
public class ShortenRequest {

	private String url;
	
	private boolean passwordEnabled;
	private String password;
	
}
