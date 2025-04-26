package tn.esprit.mindfull.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import tn.esprit.mindfull.model.User;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    private final String SECRET_KEY = "ZrbA0vVddNSLK0rk3L//Cpch/TBvpYiUx4NY126peoc=";

    public List<String> extractRoles(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Extract roles from the "roles" claim in the token
            return (List<String>) claims.get("roles");
        } catch (Exception e) {
            return Collections.emptyList(); // Return empty list if roles are missing/invalid
        }
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // Add roles to claims
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        // Add user ID to claims (cast UserDetails to your custom User class)
        if (userDetails instanceof User) { // Replace 'User' with your actual class
            claims.put("userId", ((User) userDetails).getId());
        } else {
            throw new IllegalArgumentException("Unsupported UserDetails type");
        }

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject(); // Returns null if subject is missing
        } catch (Exception e) {
            return null; // Token is invalid or expired
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            return parseToken(token).getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return true; // Treat invalid tokens as expired
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username != null
                && userDetails != null
                && username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }
    private Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()                // Use parserBuilder() for JJWT 0.11.x+
                .setSigningKey(SECRET_KEY)          // SECRET_KEY must be a Key object, not a String
                .build()
                .parseClaimsJws(token);             // Parses and validates the token
    }
    public record TokenAndExpiry(String token, Date expiry) {}

}
