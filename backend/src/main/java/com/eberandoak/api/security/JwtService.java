package com.eberandoak.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationMs;
    public JwtService(@Value("${app.jwt.secret}") String secret,@Value("${app.jwt.expiration-ms}") long expirationMs) {
        this.key=Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); this.expirationMs=expirationMs;
    }
    public String createToken(String subject) {
        Instant now=Instant.now();
        return Jwts.builder().subject(subject).issuedAt(Date.from(now)).expiration(Date.from(now.plusMillis(expirationMs))).signWith(key).compact();
    }
    public String subject(String token) { Claims claims=Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload(); return claims.getSubject(); }
}
