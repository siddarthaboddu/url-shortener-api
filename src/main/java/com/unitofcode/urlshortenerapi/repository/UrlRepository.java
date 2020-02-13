package com.unitofcode.urlshortenerapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unitofcode.urlshortenerapi.model.Url;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long>{

	public Url findFirstByShortUrl(String shortUrl);
	
	public List<Url> findAllByUserId(Long userId);
}
