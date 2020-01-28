package com.unitofcode.urlshortenerapi.dto;

import lombok.Data;

@Data
public class AccessTokenRequest {

	private String email;
	private String password;
}
