package com.unitofcode.urlshortenerapi.dto;

import lombok.Data;

@Data
public class UserRequest {

	private String email;
	private String firstName;
	private String lastName;
	private String password;
	
}
