package tn.esprit.mindfull.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import tn.esprit.mindfull.entity.User.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    private final String SECRET_KEY = "ZrbA0vVddNSLK0rk3L//Cpch/TBvpYiUx4NY126peoc=";

    // Expose SECRET_KEY for parsing in WebSocketConfig
    public String getSecretKey() {
        return SECRET_KEY;
    }

    public List<String> extractRoles(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return (List<String>) claims.get("roles");
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        if (userDetails instanceof User) {
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
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            return parseToken(token).getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username != null
                && userDetails != null
                && username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    public boolean validateTokenchat(String token) {
        return !isTokenExpired(token);
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
    }

    public record TokenAndExpiry(String token, Date expiry) {}
}