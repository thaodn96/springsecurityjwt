package com.example.demo.service;

import java.util.Optional;

import com.example.demo.model.Role;
import com.example.demo.model.RoleName;

public interface IRoleService {
	Optional<Role> findByName(RoleName name);
	
}
