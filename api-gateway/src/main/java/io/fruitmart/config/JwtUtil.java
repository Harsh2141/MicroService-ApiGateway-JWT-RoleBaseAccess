package io.fruitmart.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return this.getAllClaimsFromToken(token).getExpiration().before(new Date());
    }

    public boolean isInvalid(String token) {
        return this.validateJwtToken(token) || this.isTokenExpired(token);
    }
    private boolean validateJwtToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
			return false;
		}  catch (MalformedJwtException e) {
			System.out.println("Invalid JWT token: {}" + e.getMessage());
		} catch (ExpiredJwtException e) {
			System.out.println("JWT token is expired: {}"+ e.getMessage());
		} catch (UnsupportedJwtException e) {
			System.out.println("JWT token is unsupported: {}"+ e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println("JWT claims string is empty: {}"+ e.getMessage());
		} catch (Exception e) {
			System.out.println("-------------------------");
		}

		return true;
	}
    
    public String getRole(String token) {
    	 return this.getAllClaimsFromToken(token).get("role").toString();
    }

}
