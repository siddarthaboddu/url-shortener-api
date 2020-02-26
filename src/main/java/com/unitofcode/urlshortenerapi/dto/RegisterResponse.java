package com.unitofcode.urlshortenerapi.dto;

import com.unitofcode.urlshortenerapi.model.User;

import lombok.Data;

@Data
public class RegisterResponse {

	private String status;
	private User user;
}
