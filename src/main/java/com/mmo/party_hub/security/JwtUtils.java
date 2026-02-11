package com.mmo.party_hub.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtils {
	private static final String SECRET_KEY = "cc158e2a-0747-4f5a-84f5-1bbc20c4c048";

	public String generateToken(String username, String role) {
	    Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

	    return Jwts.builder()
	            .setSubject(username)
	            .claim("role", role)
	            .setIssuedAt(new Date())
	            .setExpiration(new Date(System.currentTimeMillis() + 86400000))
	            .signWith(key, SignatureAlgorithm.HS256)
	            .compact();
	}

    public static Claims extractClaims(String token) {
    	return Jwts.parserBuilder()
    		    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
    		    .build()
    		    .parseClaimsJws(token)
    		    .getBody();
    }
	
	public String getAuthorizedId() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
}