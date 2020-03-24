package com.unitofcode.urlshortenerapi.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
public class Visit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;
	
	private Long urlId;
	private Long userId;
	private String referer;
	private Timestamp createTime;

	@Transient
	private Long visits;

	public Visit(Long userId, Long visits, Timestamp createTime) {
		super();
		this.userId = userId;
		this.createTime = createTime;
		this.visits = visits;
	}
	
	public Visit() {
		
	}
}
