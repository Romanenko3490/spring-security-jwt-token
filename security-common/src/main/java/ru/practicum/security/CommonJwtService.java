package ru.practicum.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ru.practicum.user.Roles;

import java.util.Date;
import java.util.UUID;

public class CommonJwtService {

    protected JwtParser parser;
    protected long expiration;
    protected String secretKey;

    public CommonJwtService(String secretKey, long expiration) {
        this.secretKey = secretKey;
        this.expiration = expiration;
        this.parser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .setAllowedClockSkewSeconds(60)
                .requireIssuer("auth-service")
                .requireAudience("gateway")
                .setClock(() -> new Date())
                .build();
    }

    public boolean validateToken(String token) {
        try {
            if (!isTokenActive(token)) {
                throw new Exception("Token not Active");
            }
            if (!isAudienceValid(token)) {
                throw new Exception("Token Audience Invalid");
            }
            if (isTokenExpired(token)) {
                throw new Exception("Token Expired");
            }
            if (!isIssuerValid(token)) {
                throw new Exception("Token Issuer Invalid");
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return parser.parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractEmail(String token) {
        Claims claims = parser.parseClaimsJws(token)
                .getBody();
        return claims.get("email", String.class);
    }

    //методы извлечения
    @SuppressWarnings("unchecked")
    public String extractRole(String token) {
        Claims claims = parser.parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }

    public String extractIssuer(String token) {
        return parser.parseClaimsJws(token)
                .getBody()
                .getIssuer();
    }

    public String extractAudience(String token) {
        return parser.parseClaimsJws(token)
                .getBody()
                .getAudience();
    }

    public Date extractIssuedAt(String token) {
        return parser.parseClaimsJws(token)
                .getBody()
                .getIssuedAt();
    }

    public Date extractExpiration(String token) {
        return parser.parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public Date extractNotBefore(String token) {
        return parser.parseClaimsJws(token)
                .getBody()
                .getNotBefore();
    }

    public String extractJwtId(String token) {
        return parser.parseClaimsJws(token)
                .getBody()
                .getId();
    }

    //методы проверки
    public boolean isTokenExpired(String token) {
        Date expiration = parser.parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    public boolean isAudienceValid(String token) {
        return "gateway".equals(extractAudience(token));
    }

    public boolean isIssuerValid(String token) {
        return "auth-service".equals(extractIssuer(token));
    }

    public boolean isTokenActive(String token) {
        Date notBefore = extractNotBefore(token);
        Date now = new Date();
        return notBefore == null || !now.before(notBefore);
    }

}
