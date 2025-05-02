package com.incon.backend.config;

import com.incon.backend.dto.response.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Configuration for test environment
 */
@Configuration
@Profile("test")
@EnableWebMvc
public class ApplicationTestConfig {

    /**
     * Create a default ApiResponse bean for tests
     */
    @Bean
    public ApiResponse defaultApiResponse() {
        return new ApiResponse("Operation successful", HttpStatus.OK);
    }
}