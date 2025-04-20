package com.incon.backend.config;

import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Restrict allowed origins to only your frontend
        config.addAllowedOrigin("http://app.localhost:3000");

        // Allow credentials (cookies) to be sent
        config.setAllowCredentials(true);

        // Restrict allowed methods to only what you need
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("OPTIONS");

        // Restrict allowed headers to only what’s necessary
        config.addAllowedHeader("Authorization");
        config.addAllowedHeader("Content-Type");

        // Allow specific exposed headers if needed
        config.addExposedHeader("Content-Type");

        // Set max age for CORS preflight cache (1 hour)
        config.setMaxAge(3600L);

        // Apply this configuration to all paths
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}