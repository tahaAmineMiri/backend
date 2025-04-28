package com.incon.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtConfig {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

//    @Bean
//    public JwtDecoder jwtDecoder() {
//        String jwksUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/certs";
//        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwksUrl).build();
//
//        // Configure issuer, audience, and custom validator
//        String expectedIssuer = authServerUrl + "/realms/" + realm;
//        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(
//                JwtValidators.createDefaultWithIssuer(expectedIssuer),
//                new AudienceValidator(clientId),
//                new OrganizationValidator("incon-market")
//        );
//        jwtDecoder.setJwtValidator(validator);
//
//        return jwtDecoder;
//    }

    // Custom validator for audience
    static class AudienceValidator implements OAuth2TokenValidator<Jwt> {
        private final String expectedAudience;

        public AudienceValidator(String expectedAudience) {
            this.expectedAudience = expectedAudience;
        }

        @Override
        public org.springframework.security.oauth2.core.OAuth2TokenValidatorResult validate(Jwt jwt) {
            if (!jwt.getAudience().contains(expectedAudience)) {
                return org.springframework.security.oauth2.core.OAuth2TokenValidatorResult.failure(
                        new org.springframework.security.oauth2.core.OAuth2Error(
                                "invalid_token",
                                "The audience claim does not contain the expected audience: " + expectedAudience,
                                null
                        )
                );
            }
            return org.springframework.security.oauth2.core.OAuth2TokenValidatorResult.success();
        }
    }

    static class OrganizationValidator implements OAuth2TokenValidator<Jwt> {
        private final String expectedOrganization;

        public OrganizationValidator(String expectedOrganization) {
            this.expectedOrganization = expectedOrganization;
        }

        @Override
        public org.springframework.security.oauth2.core.OAuth2TokenValidatorResult validate(Jwt jwt) {
            String organization = jwt.getClaimAsString("organization");
            if (organization == null || !organization.equals(expectedOrganization)) {
                return org.springframework.security.oauth2.core.OAuth2TokenValidatorResult.failure(
                        new org.springframework.security.oauth2.core.OAuth2Error(
                                "invalid_token",
                                "The organization claim does not match the expected value: " + expectedOrganization,
                                null
                        )
                );
            }
            return org.springframework.security.oauth2.core.OAuth2TokenValidatorResult.success();
        }
    }
}