package com.unitofcode.urlshortenerapi.repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.unitofcode.urlshortenerapi.model.Url;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long>{

	public Url findFirstByShortUrl(String shortUrl);
	
	public List<Url> findAllByUserId(Long userId);
}
