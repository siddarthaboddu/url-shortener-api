package com.unitofcode.urlshortenerapi.dto;

import lombok.Data;

@Data
public class AccessTokenResponse {

	private String accessToken;
	private String tokenType;
	private Integer expiresIn;
}
