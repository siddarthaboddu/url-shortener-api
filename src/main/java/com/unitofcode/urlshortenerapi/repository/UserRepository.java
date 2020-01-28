package com.unitofcode.urlshortenerapi.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.unitofcode.urlshortenerapi.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

	public Optional<User> findFirstByEmail(String email);
	public Optional<User> findFirstByAccessToken(String token);

}
