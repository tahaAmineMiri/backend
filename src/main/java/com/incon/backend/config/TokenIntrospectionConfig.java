package com.incon.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class TokenIntrospectionConfig {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Bean
    public OpaqueTokenIntrospector opaqueTokenIntrospector() {
        String introspectionUri = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token/introspect";
        return token -> {
            SpringOpaqueTokenIntrospector delegate = new SpringOpaqueTokenIntrospector(introspectionUri, clientId, clientSecret);
            var auth = delegate.introspect(token);
            Map<String, Object> attributes = new java.util.HashMap<>(auth.getAttributes());

            // Extract roles from realm_access
            Map<String, Object> realmAccess = (Map<String, Object>) attributes.get("realm_access");
            List<String> roles = (List<String>) realmAccess.get("roles");
            // Convert roles to a Collection<GrantedAuthority>
            Collection<GrantedAuthority> authorities = roles.stream()
                    .filter(role -> role.startsWith("ROLE_"))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // Add additional claims to attributes
            attributes.put("email", attributes.get("email"));
            attributes.put("name", attributes.get("name")); // Maps to fullName in UserResponse

            // Create and return an OAuth2AuthenticatedPrincipal with the attributes and authorities
            return new DefaultOAuth2AuthenticatedPrincipal(
                    (String) attributes.get("preferred_username"),
                    attributes,
                    authorities
            );
        };
    }
}