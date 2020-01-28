package com.unitofcode.urlshortenerapi.config;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.netflix.concurrency.limits.servlet.ConcurrencyLimitServletFilter;
import com.netflix.concurrency.limits.servlet.ServletLimiterBuilder;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableRedisRepositories
@Slf4j
public class UrlShortenerConfig {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(jedisConnectionFactory());
		return template;
	}

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
		jedisConFactory.setHostName("localhost");
		jedisConFactory.setPort(6379);
		return jedisConFactory;
	}

	@Bean
	@Order(1)
	public Filter filter() {
		Filter filter = new ConcurrencyLimitServletFilter(
				new ServletLimiterBuilder().
				partition("shorten", 0.3).
				partition("retreive", 0.5).
				partition("user", 0.2).
				build());
		return filter;

	}

}
