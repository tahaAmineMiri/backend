package com.incon.backend.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@Getter
public class JwtConfig {
    // 💡 Simple hardcoded config for MVP
    private static final String JWT_SECRET = "yourSuperSecretKeyThatShouldBeAtLeast32CharactersLong!";
    private static final long JWT_EXPIRATION = 86400000; // 24 hours in milliseconds

    private final SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    private final long expiration = JWT_EXPIRATION;
    private final SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;

    public String getIssuer() {
        return "incon-backend";
    }
}