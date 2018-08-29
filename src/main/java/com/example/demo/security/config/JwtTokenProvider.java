package com.example.demo.security.config;


import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.demo.security.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.crypto.MacProvider;

// user to generate jwt aftera user logs in successfully and 
// validating jwt in the authorization header request

@Component
public class JwtTokenProvider {
	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
	
	//@Value("${app.jwtSecret}")
	private static final String base64Secret = "mysecret";
	//private static final Key secret = MacProvider.generateKey(SignatureAlgorithm.HS256);
	//private static final byte[] secretByte = secret.getEncoded();
	//private static final String base64Secret = Base64.getEncoder().encodeToString(secretByte);
	
	//@Value("${app.jwtExpirationInMs}")
	private int jwtExpirationInMs = 604800000;
	
	public String gernateToken(Authentication authentication){
		User userPrincipal = (User) authentication.getPrincipal();
		System.out.println(this.getClass().getName() + " user info  " + userPrincipal.getId() + " " + userPrincipal.getUsername());
		
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
		
		
		String jwt =  Jwts.builder()
				.setId(userPrincipal.getId())
				.setSubject(userPrincipal.getId())
				.setIssuedAt(now)
				.setNotBefore(now)
				.setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS256,base64Secret)
				.compact();
		return jwt;
	}
	
	public String getUserIdFromJWT(String token){
		Claims claims = Jwts.parser().setSigningKey(base64Secret)
				.parseClaimsJws(token)
				.getBody();
		
		System.out.println(this.getClass().getName() + " id " + claims.getId());
		System.out.println(this.getClass().getName() + " Subject  " + claims.getSubject());
		System.out.println(this.getClass().getName() + " Exire " + claims.getExpiration());

		return claims.getSubject();
	}
	
	public boolean validateToken(String authToken) {
		try{
			//Jwts.parser().setSigningKey(base64Secret).parseClaimsJwt(authToken);

			Jwts.parser().setSigningKey(base64Secret).parse(authToken);
			return true;
		}catch(SignatureException ex){
			logger.error("Invalid JWT signature");
		}catch(MalformedJwtException ex){
			logger.error("Invalid JWT token");

		}catch(ExpiredJwtException ex){
			logger.error("Expired JWT token");

		}catch (UnsupportedJwtException ex){
			logger.error("Unspported JWT toke");

		}catch(IllegalArgumentException ex){
			logger.error("JWT Claims string is empty");

		}
		return false;
	}
}
