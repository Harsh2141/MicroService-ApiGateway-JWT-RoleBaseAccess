package io.fruitmart.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.fruitmart.dto.UserVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private String expirationTime;

	private Key key;

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
	}

	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public Date getExpirationDateFromToken(String token) {
		return getAllClaimsFromToken(token).getExpiration();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public String generate(UserVO userVO, String type) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", userVO.getId());
		claims.put("role", userVO.getRole());
		return doGenerateToken(claims, userVO.getEmail(), type);
	}

	private String doGenerateToken(Map<String, Object> claims, String username, String type) {
		long expirationTimeLong;
		if ("ACCESS".equals(type)) {
			expirationTimeLong = Long.parseLong(expirationTime) * 1000;
		} else {
			expirationTimeLong = Long.parseLong(expirationTime) * 1000 * 5;
		}
		final Date createdDate = new Date();
		final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong);

		return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(createdDate)
				.setExpiration(expirationDate).signWith(key).compact();
	}

	public Boolean validateToken(String token) {
		return !isTokenExpired(token);
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(authToken);
			return true;
		} catch (MalformedJwtException e) {
			System.out.println("Invalid JWT token: {}" + e.getMessage());
		} catch (ExpiredJwtException e) {
			System.out.println("JWT token is expired: {}" + e.getMessage());
		} catch (UnsupportedJwtException e) {
			System.out.println("JWT token is unsupported: {}" + e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println("JWT claims string is empty: {}" + e.getMessage());
		}

		return false;
	}

}
