package com.unitofcode.urlshortenerapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.unitofcode.urlshortenerapi.model.ClientUsage;

@Repository
public interface ClientUsageRepository extends CrudRepository<ClientUsage, String> {

	public ClientUsage findFirstByUserId(Long userId);

	public ClientUsage findFirstByIpAddress(String ipAddress);
	
}
