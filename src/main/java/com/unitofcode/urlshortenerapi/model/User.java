package com.unitofcode.urlshortenerapi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;
	
	private String firstName;
	private String lastName;
	private String email;
	@JsonIgnore
	private String passwordHash;
	@JsonIgnore
	private String accessToken;
	
}
