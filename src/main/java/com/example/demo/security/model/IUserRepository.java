package com.example.demo.security.model;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface IUserRepository extends MongoRepository<User, String>{

	Optional<User> findByEmail(String email);
	Optional<User> findByUsernameOrEmail(String username, String email);
	List<User> findByIdIn(List<String> userIds);
	Optional<User> findByUsername(String username);
	
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
}
