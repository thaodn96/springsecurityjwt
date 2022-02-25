package com.example.demo.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.demo.security.userprincal.UserPrinciple;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@Component
public class JwtProvider {
	private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
	private static String jwtSecret = "thaonguyen@gmail.com";
	private int jwtExpiration = 86400;
	
	public String createToken(Authentication authentication) {
		UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
		return Jwts.builder().setSubject(userPrinciple.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime()+jwtExpiration*1000))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
			return true;
		} catch(SignatureException e) {
			logger.error("Invalid JWT signature -> Message:{}");
		} catch(MalformedJwtException e) {
			logger.error("Invalid format Token -> Message:{}");
		} catch(ExpiredJwtException e) {
			logger.error("Unsupported JWT token -> Message:{}");
		} catch(IllegalArgumentException e) {
			logger.error("JWT claims string is empty -> Message {}");
		}
		return false;
	}
	
	public String getUserNameFromToken(String token) {
		String userName = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
		return userName;
	}
	
}
