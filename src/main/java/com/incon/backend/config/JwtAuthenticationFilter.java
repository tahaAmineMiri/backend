//package com.incon.backend.config;
//
//import com.incon.backend.service.JwtService;
//import com.incon.backend.service.CustomUserDetailsService;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtService jwtService;
//    private final CustomUserDetailsService userDetailsService;
//
//    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
//        this.jwtService = jwtService;
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        final String authHeader = request.getHeader("Authorization");
//        final String jwt;
//        final String userEmail;
//
//        // 💡 Check if auth header exists and has Bearer token
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        // Extract JWT token (remove "Bearer " prefix)
//        jwt = authHeader.substring(7);
//        userEmail = jwtService.extractEmail(jwt);
//
//        // 💡 If token is valid and no auth exists yet
//        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
//
//            if (jwtService.isTokenValid(jwt, userDetails)) {
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                        userDetails,
//                        null,
//                        userDetails.getAuthorities()
//                );
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//}