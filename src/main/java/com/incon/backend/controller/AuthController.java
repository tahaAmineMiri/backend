package com.incon.backend.controller;

import com.incon.backend.dto.request.AuthRequest;
import com.incon.backend.dto.response.UserResponse;
import com.incon.backend.entity.User;
import com.incon.backend.enums.Role;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Autowired
    private JwtDecoder jwtDecoder;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.frontend-client-id:incon-market-frontend}")
    private String frontendClientId;

    @Value("${keycloak.frontend-client-secret:}") // Empty if public client
    private String frontendClientSecret;

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkCookies(HttpServletRequest request) {
        System.out.println("Received /check request");
        try {
            // Extract cookies
            Cookie[] cookies = request.getCookies();
            boolean accessTokenExists = false;
            boolean refreshTokenExists = false;

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    System.out.println("Cookie found: name=" + cookie.getName() + ", value=" + cookie.getValue().substring(0, Math.min(20, cookie.getValue().length())) + "...");
                    if ("access_token".equals(cookie.getName())) {
                        accessTokenExists = true;
                    } else if ("refresh_token".equals(cookie.getName())) {
                        refreshTokenExists = true;
                    }
                }
            } else {
                System.out.println("No cookies found in request");
            }

            // Return detailed cookie status
            Map<String, Object> response = new HashMap<>();
            response.put("accessTokenExists", accessTokenExists);
            response.put("refreshTokenExists", refreshTokenExists);
            response.put("cookiesExist", accessTokenExists && refreshTokenExists);

            if (accessTokenExists && refreshTokenExists) {
                System.out.println("Cookies found: access_token and refresh_token");
                return ResponseEntity.ok(response);
            } else {
                System.out.println("Cookies missing: access_token=" + accessTokenExists + ", refresh_token=" + refreshTokenExists);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            System.out.println("Failed to check cookies: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to check cookies: " + e.getMessage()));
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, String>> validateToken(@RequestBody(required = false) AuthRequest request, HttpServletRequest httpRequest) {
        try {
            String accessToken = null;

            // Step 1: Try to get access token from cookies (prioritize this for dashboard refresh)
            Cookie[] cookies = httpRequest.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("access_token".equals(cookie.getName())) {
                        accessToken = cookie.getValue();
                        System.out.println("Validating token from cookies: " + accessToken.substring(0, 20) + "...");
                        break;
                    }
                }
            }

            // Step 2: If not found in cookies, try to get access token from request body (for LoginPage)
            if (accessToken == null && request != null && request.getAccessToken() != null && !request.getAccessToken().isEmpty()) {
                accessToken = request.getAccessToken();
                System.out.println("Validating token from request body: " + accessToken.substring(0, 20) + "...");
            }

            // Step 3: If no token is found, return an error
            if (accessToken == null || accessToken.isEmpty()) {
                System.out.println("No access token provided");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Access token not provided"));
            }

            // Step 4: Validate the token
            Jwt jwt;
            try {
                jwt = jwtDecoder.decode(accessToken);
            } catch (Exception e) {
                System.out.println("JWT decoding failed: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid access token: " + e.getMessage()));
            }

            if (jwt == null) {
                System.out.println("JWT decoding returned null");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid access token"));
            }

            // Log decoded information
            System.out.println("Decoded JWT Claims: " + jwt.getClaims());
            System.out.println("Subject (sub): " + jwt.getSubject());
            System.out.println("Username (preferred_username): " + jwt.getClaimAsString("preferred_username"));
            System.out.println("Email: " + jwt.getClaimAsString("email"));
            Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaim("realm_access");
            List<String> roles = realmAccess != null ? (List<String>) realmAccess.get("roles") : List.of();
            System.out.println("Roles: " + roles);

            String username = jwt.getClaimAsString("preferred_username");
            String email = jwt.getClaimAsString("email");
            if (username == null || email == null) {
                System.out.println("Missing required claims: username=" + username + ", email=" + email);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Missing required claims in access token"));
            }

            String roleString = roles.stream()
                    .filter(role -> role.equals("BUYER") || role.equals("SELLER") || role.equals("ADMIN"))
                    .findFirst()
                    .orElse(null);
            System.out.println("role: " + roleString);
            if (roleString == null) {
                System.out.println("No valid role found in token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User does not have a valid role (BUYER or SELLER required)"));
            }

            System.out.println("Validation successful, role: " + roleString);
            return ResponseEntity.ok(Map.of("role", roleString));
        } catch (Exception e) {
            System.out.println("Validation error: " + e.getMessage());
            e.printStackTrace(); // Add stack trace for better debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to validate token: " + e.getMessage()));
        }
    }

    @PostMapping("/store-tokens")
    public ResponseEntity<Map<String, String>> storeTokens(@RequestBody AuthRequest request, HttpServletResponse response) {
        System.out.println("Received /store-tokens request");
        try {
            String accessToken = request.getAccessToken();
            String refreshToken = request.getRefreshToken();
            Integer accessTokenTTL = request.getAccessTokenTTL();
            Integer refreshTokenTTL = request.getRefreshTokenTTL();

            if (accessToken == null || accessToken.isEmpty()) {
                System.out.println("No access token provided in request");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Access token is required"));
            }
            if (refreshToken == null || refreshToken.isEmpty()) {
                System.out.println("No refresh token provided in request");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Refresh token is required"));
            }

            System.out.println("Access token TTL: " + accessTokenTTL + " seconds");
            System.out.println("Refresh token TTL: " + refreshTokenTTL + " seconds");

            // Use TTLs if needed (e.g., set cookie maxAge dynamically)
            ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                    .maxAge(accessTokenTTL != null ? accessTokenTTL : 3600) // Default to 1 hour if null
                    .path("/")
                    .secure(true)
                    .httpOnly(true)
                    .sameSite("None")
                    .build();

            ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                    .maxAge(refreshTokenTTL != null ? refreshTokenTTL : 604800) // Default to 7 days if null
                    .path("/")
                    .secure(true)
                    .httpOnly(true)
                    .sameSite("None")
                    .build();

            response.addHeader("Set-Cookie", accessCookie.toString());
            response.addHeader("Set-Cookie", refreshCookie.toString());

            return ResponseEntity.ok(Map.of("message", "Tokens stored successfully"));
        } catch (Exception e) {
            System.out.println("Failed to store tokens: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to store tokens: " + e.getMessage()));
        }
    }

    @GetMapping("/userinfo")
    public ResponseEntity<Map<String, Object>> getUserInfo(HttpServletRequest request) {
        try {
            String accessToken = null;
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("access_token".equals(cookie.getName())) {
                        accessToken = cookie.getValue();
                        break;
                    }
                }
            }

            if (accessToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    authServerUrl + "/realms/" + realm + "/protocol/openid-connect/userinfo",
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    Map.class
            );

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch user info: " + e.getMessage()));
        }
    }

    @PostMapping("/refresh-tokens")
    public ResponseEntity<Map<String, String>> refreshTokens(HttpServletRequest request, HttpServletResponse response) {
        try {
            String refreshToken = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("refresh_token".equals(cookie.getName())) {
                        refreshToken = cookie.getValue();
                        break;
                    }
                }
            }

            if (refreshToken == null) {
                System.out.println("Refresh token not found in cookies");
                return ResponseEntity.badRequest().body(Map.of("error", "Refresh token not found in cookies"));
            }

            String tokenEndpoint = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("grant_type", "refresh_token");
            formData.add("client_id", frontendClientId); // Use incon-market-frontend
            if (!frontendClientSecret.isEmpty()) {
                formData.add("client_secret", frontendClientSecret);
            }
            formData.add("refresh_token", refreshToken);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);
            ResponseEntity<Map> refreshResponse = restTemplate.exchange(
                    tokenEndpoint,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (refreshResponse.getStatusCode() != HttpStatus.OK || refreshResponse.getBody() == null) {
                System.out.println("Failed to refresh tokens: " + refreshResponse.getBody());
                return ResponseEntity.badRequest().body(Map.of("error", "Failed to refresh tokens: " + refreshResponse.getBody()));
            }

            Map<String, Object> responseBody = refreshResponse.getBody();
            String newAccessToken = (String) responseBody.get("access_token");
            String newRefreshToken = (String) responseBody.get("refresh_token");

            if (newAccessToken == null || newRefreshToken == null) {
                System.out.println("Missing tokens in refresh response: access_token=" + newAccessToken + ", refresh_token=" + newRefreshToken);
                return ResponseEntity.badRequest().body(Map.of("error", "Missing tokens in refresh response"));
            }

            Jwt jwt = jwtDecoder.decode(newAccessToken);
            if (jwt == null) {
                System.out.println("Invalid new access token after refresh");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid new access token after refresh"));
            }

            System.out.println("New JWT Claims after refresh: " + jwt.getClaims());
            Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaim("realm_access");
            List<String> roles = realmAccess != null ? (List<String>) realmAccess.get("roles") : List.of();
            String roleString = roles.stream()
                    .filter(role -> role.equals("BUYER") || role.equals("SELLER") || role.equals("ADMIN"))
                    .findFirst()
                    .orElse(null);
            if (roleString == null) {
                System.out.println("No valid role found in new token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No valid role found in new token"));
            }

            ResponseCookie accessCookie = ResponseCookie.from("access_token", newAccessToken)
                    .maxAge(5) // 5 seconds (for testing)
                    .path("/")
                    .secure(true)
                    .httpOnly(true)
                    .sameSite("None")
                    .build();

            ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", newRefreshToken)
                    .maxAge(10) // 10 seconds (for testing)
                    .path("/")
                    .secure(true)
                    .httpOnly(true)
                    .sameSite("None")
                    .build();

            response.addHeader("Set-Cookie", accessCookie.toString());
            response.addHeader("Set-Cookie", refreshCookie.toString());
            System.out.println("Set-Cookie headers added: " + accessCookie.toString() + ", " + refreshCookie.toString());

            return ResponseEntity.ok(Map.of("message", "Tokens refreshed successfully"));
        } catch (Exception e) {
            System.out.println("Failed to refresh tokens: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to refresh tokens: " + e.getMessage()));
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Get refresh token from cookies
            String refreshToken = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("refresh_token".equals(cookie.getName())) {
                        refreshToken = cookie.getValue();
                        break;
                    }
                }
            }

            // Call Keycloak's logout endpoint if refresh token exists
            if (refreshToken != null) {
                String logoutEndpoint = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/logout";
                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                formData.add("client_id", frontendClientId);
                if (!frontendClientSecret.isEmpty()) {
                    formData.add("client_secret", frontendClientSecret);
                }
                formData.add("refresh_token", refreshToken);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);
                ResponseEntity<String> logoutResponse = restTemplate.postForEntity(logoutEndpoint, entity, String.class);

                if (logoutResponse.getStatusCode() != HttpStatus.NO_CONTENT) {
                    System.out.println("Failed to terminate Keycloak session: " + logoutResponse.getBody());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("error", "Failed to terminate Keycloak session"));
                }
                System.out.println("Keycloak session terminated successfully");
            } else {
                System.out.println("No refresh token found for Keycloak logout");
            }

            // Clear cookies using ResponseCookie for consistency
            ResponseCookie accessCookie = ResponseCookie.from("access_token", "")
                    .maxAge(0)
                    .path("/")
                    .secure(true)
                    .httpOnly(true)
                    .sameSite("None")
                    .build();

            ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", "")
                    .maxAge(0)
                    .path("/")
                    .secure(true)
                    .httpOnly(true)
                    .sameSite("None")
                    .build();

            response.addHeader("Set-Cookie", accessCookie.toString());
            response.addHeader("Set-Cookie", refreshCookie.toString());

            return ResponseEntity.ok().body(Map.of("message", "Logged out successfully"));
        } catch (Exception e) {
            System.out.println("Failed to logout: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to logout: " + e.getMessage()));
        }
    }
}