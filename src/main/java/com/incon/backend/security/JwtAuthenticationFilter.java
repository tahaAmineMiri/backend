package com.incon.backend.security;

import com.incon.backend.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String accessToken = null;
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("access_token".equals(cookie.getName())) {
                        accessToken = cookie.getValue();
                        System.out.println("Access token found in cookie: " + accessToken);
                        break;
                    }
                }
            }

            if (accessToken == null) {
                System.out.println("No access token found in cookies. Skipping authentication.");
                filterChain.doFilter(request, response);
                return;
            }

            // Validate token and check if it's active
            if (tokenProvider.validateToken(accessToken) && tokenProvider.introspectToken(accessToken)) {
                // Extract claims
                Claims claims = tokenProvider.getClaims(accessToken);
                String role = tokenProvider.getRoleFromToken(claims);
                System.out.println("Token is valid. Role extracted: " + role);

                // Create authentication token with the role as an authority
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        role, // Principal (can be username or user ID; using role for simplicity)
                        null, // Credentials (not needed after token validation)
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)) // Authorities
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                System.out.println("Authentication set in security context.");
            } else {
                System.out.println("Token is invalid or revoked.");
                throw new UnauthorizedException("Invalid or revoked token");
            }
        } catch (UnauthorizedException e) {
            System.out.println("UnauthorizedException: " + e.getMessage());
            throw e; // Let JwtAuthenticationEntryPoint handle this
        } catch (Exception e) {
            System.out.println("Authentication error: " + e.getMessage());
            throw new UnauthorizedException("Authentication failed: " + e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }
}