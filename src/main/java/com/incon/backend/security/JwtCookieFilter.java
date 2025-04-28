package com.incon.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtCookieFilter extends OncePerRequestFilter {

    @Autowired
    private JwtDecoder jwtDecoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    System.out.println("token = token.getValue():" + token);
                    break;
                }
            }
        }

        // Add logging here
        System.out.println("JwtCookieFilter - Raw token extracted from cookie: '" + token + "'");
        if (token != null) {
            System.out.println("JwtCookieFilter - Token contains dots: " + token.contains("."));
        }

        if (token != null) {
            try {
                Jwt jwt = jwtDecoder.decode(token);
                Map<String, Object> realmAccess = jwt.getClaim("realm_access");
                Collection<GrantedAuthority> authorities;

                if (realmAccess != null) {
                    List<String> roles = (List<String>) realmAccess.get("roles");
                    if (roles != null) {
                        authorities = roles.stream()
                                .map(role -> new SimpleGrantedAuthority(role.toUpperCase()))
                                .collect(Collectors.toList());
                    } else {
                        authorities = Collections.emptyList(); // No roles found
                    }
                } else {
                    authorities = Collections.emptyList(); // No realm_access found
                }

// Set the authentication object with the authorities
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        jwt.getSubject(), null, authorities
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (JwtException e) {
                System.out.println("Invalid JWT: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}

