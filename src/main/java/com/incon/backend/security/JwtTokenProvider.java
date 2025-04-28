package com.incon.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incon.backend.exception.SecurityException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtTokenProvider {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Map<String, PublicKey> keyCache = new ConcurrentHashMap<>();
    private final Map<String, Long> lastIntrospectionTime = new ConcurrentHashMap<>();
    private static final long INTROSPECTION_INTERVAL = 5 * 60 * 1000; // 5 minutes

    @PostConstruct
    public void init() {
        try {
            fetchPublicKeys();
        } catch (Exception e) {
            throw new SecurityException("Failed to initialize JwtTokenProvider: Unable to fetch Keycloak public keys", e);
        }
    }

    private void fetchPublicKeys() {
        String jwksUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/certs";
        Map<String, Object> jwks = restTemplate.getForObject(jwksUrl, Map.class);
        if (jwks == null || !jwks.containsKey("keys")) {
            throw new SecurityException("Failed to fetch Keycloak JWKS: No keys found");
        }

        List<Map<String, Object>> keys = (List<Map<String, Object>>) jwks.get("keys");
        for (Map<String, Object> key : keys) {
            if ("RS256".equals(key.get("alg"))) {
                String kid = (String) key.get("kid");
                String modulus = (String) key.get("n");
                String exponent = (String) key.get("e");

                try {
                    PublicKey publicKey = generatePublicKey(modulus, exponent);
                    keyCache.put(kid, publicKey);
                } catch (Exception e) {
                    throw new SecurityException("Failed to parse public key for kid: " + kid, e);
                }
            }
        }
    }

    private PublicKey generatePublicKey(String modulus, String exponent) throws Exception {
        byte[] modulusBytes = Base64.getUrlDecoder().decode(modulus);
        byte[] exponentBytes = Base64.getUrlDecoder().decode(exponent);

        java.security.spec.RSAPublicKeySpec spec = new java.security.spec.RSAPublicKeySpec(
                new java.math.BigInteger(1, modulusBytes),
                new java.math.BigInteger(1, exponentBytes)
        );

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    public boolean validateToken(String token) {
        try {
            System.out.println("JwtTokenProvider - Validating token: '" + token + "'");
//            System.out.println("JwtTokenProvider - Token contains dots: " + (token != null && token.contains(".")));
//            System.out.println("JwtTokenProvider - Token bytes: " + Arrays.toString(token.getBytes()));

            String[] parts = token.split("\\.");
//            System.out.println("JwtTokenProvider - Number of parts after split: " + parts.length);
//            for (int i = 0; i < parts.length; i++) {
//                System.out.println("JwtTokenProvider - Part " + i + ": '" + parts[i] + "'");
//            }
            if (parts.length != 3) {
                System.out.println("JwtTokenProvider - Token has invalid format, parts length: " + parts.length);
                throw new SecurityException("Invalid JWT token format");
            }

            // Decode the header and parse it as JSON
            String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]));
            System.out.println("JwtTokenProvider - Decoded header JSON: " + headerJson);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> header;
            try {
                header = mapper.readValue(headerJson, Map.class);
            } catch (Exception e) {
                System.out.println("JwtTokenProvider - Failed to parse header as JSON: " + e.getMessage());
                throw new SecurityException("Invalid JWT header format: " + e.getMessage());
            }

            String kid = (String) header.get("kid");
            if (kid == null || kid.isEmpty()) {
                System.out.println("JwtTokenProvider - No kid found in token header");
                throw new SecurityException("JWT token header missing kid field");
            }
            System.out.println("JwtTokenProvider - Extracted kid from token header: " + kid);

            PublicKey publicKey = keyCache.get(kid);
            if (publicKey == null) {
                System.out.println("JwtTokenProvider - Public key not found in cache for kid: " + kid + ", fetching new keys");
                fetchPublicKeys();
                publicKey = keyCache.get(kid);
                if (publicKey == null) {
                    System.out.println("JwtTokenProvider - Public key still not found for kid: " + kid);
                    throw new SecurityException("Public key not found for kid: " + kid);
                }
            }

            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token);

            Claims claims = claimsJws.getBody();

            // Validate issuer
            if (!claims.getIssuer().equals(authServerUrl + "/realms/" + realm)) {
                System.out.println("JwtTokenProvider - Invalid issuer in token: " + claims.getIssuer());
                throw new SecurityException("Invalid issuer in token");
            }

            // Validate audience
            Object audClaim = claims.get("aud");
            boolean isAudienceValid = false;

            if (audClaim instanceof String) {
                isAudienceValid = audClaim.equals(clientId);
            } else if (audClaim instanceof List) {
                List<?> audiences = (List<?>) audClaim;
                isAudienceValid = audiences.contains(clientId);
            }

            String azp = claims.get("azp", String.class);
            List<String> trustedClients = Arrays.asList("incon-market-frontend");
            if (!isAudienceValid && (azp == null || !trustedClients.contains(azp))) {
                System.out.println("JwtTokenProvider - Invalid audience in token. Expected: " + clientId + ", Found aud: " + audClaim + ", azp: " + azp);
                throw new SecurityException("Invalid audience in token");
            }

            System.out.println("JwtTokenProvider - Token validated successfully");
            return true;
        } catch (JwtException e) {
            System.out.println("JwtTokenProvider - Token validation failed: " + e.getMessage());
            throw new SecurityException("Token validation failed: " + e.getMessage(), e);
        }
    }

    // New method to get Claims after validation
    public Claims getClaims(String token) {
        try {
            System.out.println("JwtTokenProvider - Extracting claims from token: '" + token + "'");
            System.out.println("JwtTokenProvider - Token contains dots: " + (token != null && token.contains(".")));
            System.out.println("JwtTokenProvider - Token bytes: " + Arrays.toString(token.getBytes()));

            String[] parts = token.split("\\.");
            System.out.println("JwtTokenProvider - Number of parts after split: " + parts.length);
            for (int i = 0; i < parts.length; i++) {
                System.out.println("JwtTokenProvider - Part " + i + ": '" + parts[i] + "'");
            }

            String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]));
            System.out.println("JwtTokenProvider - Decoded header JSON: " + headerJson);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> header;
            try {
                header = mapper.readValue(headerJson, Map.class);
            } catch (Exception e) {
                System.out.println("JwtTokenProvider - Failed to parse header as JSON: " + e.getMessage());
                throw new SecurityException("Invalid JWT header format: " + e.getMessage());
            }

            String kid = (String) header.get("kid");
            if (kid == null || kid.isEmpty()) {
                System.out.println("JwtTokenProvider - No kid found in token header");
                throw new SecurityException("JWT token header missing kid field");
            }

            PublicKey publicKey = keyCache.get(kid);
            if (publicKey == null) {
                System.out.println("JwtTokenProvider - Public key not found for kid: " + kid + ", fetching new keys");
                fetchPublicKeys();
                publicKey = keyCache.get(kid);
                if (publicKey == null) {
                    throw new SecurityException("Public key not found for kid: " + kid);
                }
            }

            return Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            System.out.println("JwtTokenProvider - Failed to extract claims: " + e.getMessage());
            throw new SecurityException("Failed to extract claims: " + e.getMessage(), e);
        }
    }

    public boolean introspectToken(String token) {
        try {
            long currentTime = System.currentTimeMillis();
            Long lastCheck = lastIntrospectionTime.getOrDefault(token, 0L);
            if (currentTime - lastCheck <= INTROSPECTION_INTERVAL) {
                System.out.println("JwtTokenProvider - Token introspection skipped, within interval: " + token);
                return true;
            }

            HttpRequest introspectionRequest = HttpRequest.newBuilder()
                    .uri(URI.create(authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token/introspect"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "token=" + token +
                                    "&client_id=" + clientId +
                                    "&client_secret=" + clientSecret
                    ))
                    .build();

            HttpResponse<String> introspectionResponse = httpClient.send(
                    introspectionRequest,
                    HttpResponse.BodyHandlers.ofString()
            );

            String responseBody = introspectionResponse.body();
            if (introspectionResponse.statusCode() != 200 || !responseBody.contains("\"active\":true")) {
                System.out.println("JwtTokenProvider - Token introspection failed, response: " + responseBody);
                return false;
            }

            lastIntrospectionTime.put(token, currentTime);
            System.out.println("JwtTokenProvider - Token introspected successfully");
            return true;
        } catch (Exception e) {
            System.out.println("JwtTokenProvider - Token introspection failed: " + e.getMessage());
            throw new SecurityException("Token introspection failed: " + e.getMessage(), e);
        }
    }

    public String getRoleFromToken(Claims claims) {
        try {
            System.out.println("JwtTokenProvider - Extracting role from claims");

            Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");
            if (realmAccess == null || !realmAccess.containsKey("roles")) {
                System.out.println("JwtTokenProvider - No realm_access or roles found in token");
                throw new SecurityException("No roles found in token");
            }

            List<String> roles = (List<String>) realmAccess.get("roles");
            if (roles == null || roles.isEmpty()) {
                System.out.println("JwtTokenProvider - Roles list is empty");
                throw new SecurityException("No roles found in token");
            }

            String role = roles.stream()
                    .filter(r -> r.equals("BUYER") || r.equals("SELLER"))
                    .findFirst()
                    .orElseThrow(() -> {
                        System.out.println("JwtTokenProvider - No target role found in roles: " + roles);
                        return new SecurityException("No target role found in token");
                    });

            System.out.println("JwtTokenProvider - Role extracted: " + role);
            return role;
        } catch (Exception e) {
            System.out.println("JwtTokenProvider - Failed to extract role: " + e.getMessage());
            throw new SecurityException("Failed to extract role: " + e.getMessage(), e);
        }
    }
}