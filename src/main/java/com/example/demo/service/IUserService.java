package com.example.demo.service;

import java.util.Optional;

import com.example.demo.model.User;

public interface IUserService {
	Optional<User> findByUsername(String name);
	Boolean existsByUsername(String username);
	Boolean existByEmail(String email);
	User save(User user); 
}
