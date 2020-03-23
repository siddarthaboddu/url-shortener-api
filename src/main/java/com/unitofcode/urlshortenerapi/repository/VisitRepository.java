package com.unitofcode.urlshortenerapi.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.unitofcode.urlshortenerapi.model.Visit;

@Repository
public interface VisitRepository extends CrudRepository<Visit, Long>{
	
	@Modifying
	public int deleteByCreateTimeBefore(Timestamp createTime);

}
