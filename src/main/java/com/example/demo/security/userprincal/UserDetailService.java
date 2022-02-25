package com.example.demo.security.userprincal;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.demo.model.User;
import com.example.demo.repository.IUserRepository;

@Component
public class UserDetailService implements UserDetailsService{
	
	@Autowired
	IUserRepository userRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found"+username));
		return UserPrinciple.build(user);
	}

}
