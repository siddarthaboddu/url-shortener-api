package com.unitofcode.urlshortenerapi.dto;

import lombok.Data;

@Data
public class Status {
	private String status;
	
	public Status(boolean status) {
		this.status = ""+status;
	}
	
	public Status(String status) {
		this.status = status;
	}
}
