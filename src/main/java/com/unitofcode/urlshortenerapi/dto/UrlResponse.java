package com.unitofcode.urlshortenerapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class UrlResponse {
	private String originalUrl;
	private String shortUrl;
	private Long usageLeft;
}
