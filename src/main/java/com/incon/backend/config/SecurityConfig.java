package com.incon.backend.config;

import com.incon.backend.entity.User;
import com.incon.backend.repository.UserRepository;
import com.incon.backend.security.JwtAuthenticationEntryPoint;
import com.incon.backend.security.JwtAuthenticationFilter;
import com.incon.backend.security.JwtCookieFilter;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    @Lazy
    private JwtCookieFilter jwtCookieFilter;

    @Autowired
    private OpaqueTokenIntrospector opaqueTokenIntrospector;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new CorsConfiguration();
                    corsConfig.setAllowedOrigins(Arrays.asList("http://app.localhost:3000"));
                    corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
                    corsConfig.setExposedHeaders(Arrays.asList("Content-Type"));
                    corsConfig.setAllowCredentials(true); // Required for cookies
                    corsConfig.setMaxAge(3600L);
                    return corsConfig;
                }))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Ensure stateless
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
//                .headers(headers -> headers
//                        .contentSecurityPolicy(csp -> csp
//                                .policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline';")
//                        )
//                        .httpStrictTransportSecurity(hsts -> hsts
//                                .includeSubDomains(true)
//                                .maxAgeInSeconds(31536000)
//                        )
//                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/").permitAll()
//                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/api/auth/store-tokens").permitAll()
                        .requestMatchers("/api/auth/check").permitAll()
                        .requestMatchers("/api/auth/validate").permitAll()
                        .requestMatchers("/api/auth/logout").permitAll()
                        .requestMatchers("/api/auth/refresh-tokens").authenticated()
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/actuator/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/buyer/**").hasAuthority("BUYER")
                        .requestMatchers("/api/seller/**").hasAuthority("SELLER")
                        .requestMatchers("/api/protected-payement").hasAnyAuthority("BUYER", "SELLER")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                                .bearerTokenResolver(new CookieBearerTokenResolver())
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
//                        .opaqueToken(opaque -> opaque.introspector(opaqueTokenIntrospector))
//                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .logout(logout -> logout
                        .logoutUrl("api/auth/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(false) // No session to invalidate
                        .clearAuthentication(true)
//                        .deleteCookies("JSESSIONID")
                )

                .addFilterBefore(jwtCookieFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    public class CookieBearerTokenResolver implements BearerTokenResolver {
        @Override
        public String resolve(HttpServletRequest request) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("access_token".equals(cookie.getName())) {
                        return cookie.getValue();
                    }
                }
            }
            return null;
        }
    }
    class CustomOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

        private final OpaqueTokenIntrospector delegate =
                new org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector(
                        "http://keycloak.app.localhost:8088/realms/incon-market-realm/protocol/openid-connect/token/introspect",
                        "incon-market-frontend",
                        "your-client-secret-here" // Replace with your client secret
                );

        @Override
        public OAuth2AuthenticatedPrincipal introspect(String token) {
            OAuth2AuthenticatedPrincipal principal = delegate.introspect(token);
            Map<String, Object> attributes = principal.getAttributes();

            // Extract roles from the introspection response
            Map<String, Object> realmAccess = (Map<String, Object>) attributes.getOrDefault("realm_access", Map.of());
            List<String> roles = (List<String>) realmAccess.getOrDefault("roles", List.of());

            // Filter and map roles to authorities
            Collection<GrantedAuthority> authorities = roles.stream()
                    .filter(role -> role.equals("BUYER") || role.equals("SELLER") || role.equals("ADMIN"))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            System.out.println("Authorities from introspection: " + authorities);

            // Create a new principal with the updated authorities
            return new DefaultOAuth2AuthenticatedPrincipal(
                    principal.getName(),
                    attributes,
                    authorities
            );
        }

    }
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            System.out.println("JWT claims: " + jwt.getClaims());
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            System.out.println("Realm access: " + realmAccess);
            if (realmAccess != null) {
                Object rolesObj = realmAccess.get("roles");
                System.out.println("Roles: " + rolesObj);
                if (rolesObj instanceof List) {
                    List<?> rolesList = (List<?>) rolesObj;
                    List<String> roles = rolesList.stream()
                            .filter(item -> item instanceof String)
                            .map(item -> (String) item)
                            .collect(Collectors.toList());
                    List<GrantedAuthority> authorities = roles.stream()
                            .filter(role -> role.equals("BUYER") || role.equals("SELLER") || role.equals("ADMIN"))
                            .map(role -> new SimpleGrantedAuthority(role.toUpperCase()))
                            .collect(Collectors.toList());
                    System.out.println("Extracted authorities: " + authorities);
                    return authorities;
                }
            }
            return List.of();
        });
        return converter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        // Use Keycloak's JWKS endpoint to fetch the public key for JWT validation
        String jwksUri = "http://keycloak.app.localhost:8088/realms/incon-market-realm/protocol/openid-connect/certs";
        return NimbusJwtDecoder.withJwkSetUri(jwksUri).build();
    }
    // Custom filter to extract token from cookie
    public static class TokenFromCookieFilter extends OncePerRequestFilter {

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            // Skip filter for /api/auth/store-tokens to avoid infinite loop
            if (request.getRequestURI().equals("/api/auth/store-tokens")) {
                filterChain.doFilter(request, response);
                return;
            }

            // Extract access_token from cookies
            final String token = (request.getCookies() != null) ?
                    Arrays.stream(request.getCookies())
                            .filter(cookie -> "access_token".equals(cookie.getName()))
                            .map(Cookie::getValue)
                            .findFirst()
                            .orElse(null) : null;

            // If token is found, wrap the request to add the Bearer token to the Authorization header
            if (token != null) {
                try {
                    HttpServletRequest wrappedRequest = new HttpServletRequestWrapper(request, token);
                    filterChain.doFilter(wrappedRequest, response);
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                    return;
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }

    // HttpServletRequestWrapper to override headers
    private static class HttpServletRequestWrapper extends jakarta.servlet.http.HttpServletRequestWrapper {
        private final String token;

        public HttpServletRequestWrapper(HttpServletRequest request, String token) {
            super(request);
            this.token = token;
        }

        @Override
        public String getHeader(String name) {
            if ("Authorization".equalsIgnoreCase(name)) {
                return "Bearer " + token;
            }
            return super.getHeader(name);
        }
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.keycloakClientRegistration());
    }

    private ClientRegistration keycloakClientRegistration() {
        return ClientRegistration.withRegistrationId("keycloak")
                .clientId("incon-market-frontend")
                .authorizationGrantType(org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://app.localhost:3000/login")
                .scope("openid", "profile", "email")
                .authorizationUri("http://app.localhost:8080/realms/incon-market-realm/protocol/openid-connect/auth")
                .tokenUri("http://app.localhost:8080/realms/incon-market-realm/protocol/openid-connect/token")
                .userInfoUri("http://app.localhost:8080/realms/incon-market-realm/protocol/openid-connect/userinfo")
                .userNameAttributeName("preferred_username")
                .jwkSetUri("http://app.localhost:8080/realms/incon-market-realm/protocol/openid-connect/certs")
                .issuerUri("http://app.localhost:8080/realms/incon-market-realm")
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userRepository.findByEmail(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRole().name())
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}