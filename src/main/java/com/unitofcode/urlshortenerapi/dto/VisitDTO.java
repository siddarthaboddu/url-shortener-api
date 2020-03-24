package com.unitofcode.urlshortenerapi.dto;

import java.util.Date;

import lombok.Data;

@Data
public class VisitDTO {

	private Long userId;
	private Long visit;
	private Date date;
	
	public VisitDTO(Long userId, Long visit, Date date) {
		super();
		this.userId = userId;
		this.visit = visit;
		this.date = date;
	}

}



/*
 * @Data public class VisitDTO {
 * 
 * private Date name; private Long value;
 * 
 * 
 * public VisitDTO(Long visit, Date date) { super(); this.value = visit;
 * this.name = date; }
 * 
 * 
 * }
 */