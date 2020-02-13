package com.unitofcode.urlshortenerapi.config;

import java.io.IOException;
import java.time.Duration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;

//@Component
//@Order(2)
@Slf4j
public class ThrottlingFilter implements Filter{ //currently disabled this approach
	
	@Autowired
	private Bucket bucket;
	
	@Bean
	private Bucket createNewBucket() {
		long overdraft = 100;
		Refill refill = Refill.greedy(300,  Duration.ofSeconds(1));
		Bandwidth limit = Bandwidth.classic(overdraft,  refill);
		return Bucket4j.builder().addLimit(limit).build();
	}
	
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        
        log.info("bucket tokens : {}",bucket.getAvailableTokens());
        // tryConsume returns false immediately if no tokens available with the bucket
        if (bucket.tryConsume(1)) {
            // the limit is not exceeded
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            // limit is exceeded
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setContentType("text/plain");
            httpResponse.setStatus(429);
            httpResponse.getWriter().append("Too many requests");
        }
    }
}
