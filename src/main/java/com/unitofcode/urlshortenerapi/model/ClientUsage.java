package com.unitofcode.urlshortenerapi.model;

import java.io.Serializable;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.Data;

@Data
@RedisHash(value="ClientUsage",timeToLive = 600)
public class ClientUsage implements Serializable{
	
	@Id
	private String id;
	@Indexed
	private String ipAddress;
	@Indexed
	private Long userId;
	private Long usageLeft;

}
