package ru.practicum.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.user.Roles;

import java.util.Date;
import java.util.UUID;

@Service
public class JwtService extends CommonJwtService {

    public JwtService(@Value("${jwt.secret}") String secretKey,
                      @Value("${jwt.expiration}") long expiration) {
        super(secretKey, expiration);
    }

    public String generateToken(String username, String email, Roles role) {
        Claims claims = Jwts.claims()
                .setId(UUID.randomUUID().toString())
                .setSubject(username)
                .setIssuer("auth-service")
                .setNotBefore(new Date())
                .setAudience("gateway");

        claims.put("role", role.name());
        claims.put("email", email);

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
