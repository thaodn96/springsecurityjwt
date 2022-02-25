package com.example.demo.controller;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.SignUpForm;
import com.example.demo.dto.response.JwtResponse;
import com.example.demo.dto.response.ResponseMessenger;
import com.example.demo.model.Role;
import com.example.demo.model.RoleName;
import com.example.demo.model.User;
import com.example.demo.security.jwt.JwtProvider;
import com.example.demo.security.userprincal.UserPrinciple;
import com.example.demo.service.iplmservice.RoleServiceIplm;
import com.example.demo.service.iplmservice.UserServiceIplm;

@RequestMapping("/api/auth")
@RestController
public class AuthController {
	
	@Autowired
	private UserServiceIplm userService;
	@Autowired
	private RoleServiceIplm roleService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtProvider jwtProvider;
	
	@PostMapping("/signup")
	public ResponseEntity<?> register(@Valid @RequestBody SignUpForm signUpForm){
		if(userService.existsByUsername(signUpForm.getUsername())) {
			return new ResponseEntity<>(new ResponseMessenger("User existed!"),HttpStatus.OK);
		}
		if(userService.existByEmail(signUpForm.getEmail())) {
			return new ResponseEntity<>(new ResponseMessenger("Email existed"),HttpStatus.OK);
		}
		User user = new User(signUpForm.getName(),signUpForm.getUsername(),signUpForm.getEmail(),passwordEncoder.encode(signUpForm.getPassword()));
		Set<String> strRoles = signUpForm.getRoles();
		Set<Role> roles = new HashSet<Role>();
		
		strRoles.forEach(role ->{
			switch (role) {
			case "admin":
				Role adminRole = roleService.findByName(RoleName.ADMIN).orElseThrow(()-> new RuntimeException("Role not found"));
				roles.add(adminRole);
				break;
				
			case "pm":
				Role pmRole = roleService.findByName(RoleName.PM).orElseThrow(()-> new RuntimeException("Role not found"));
				roles.add(pmRole);
				break;
			default:
				Role userRole = roleService.findByName(RoleName.USER).orElseThrow(()-> new RuntimeException("Role not found"));
				roles.add(userRole);
				break;
			}
		});
		user.setRoles(roles);
		userService.save(user);
		return new ResponseEntity<>(new ResponseMessenger("Create user success"),HttpStatus.OK);
	}
	
	@PostMapping("/signin")
	public ResponseEntity<?> login(@Valid @RequestBody SignUpForm signUpForm){
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(signUpForm.getUsername(), signUpForm.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtProvider.createToken(authentication);
		UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
		
		return ResponseEntity.ok(new JwtResponse(token,userPrinciple.getName(),userPrinciple.getAuthorities()));
	}
}
