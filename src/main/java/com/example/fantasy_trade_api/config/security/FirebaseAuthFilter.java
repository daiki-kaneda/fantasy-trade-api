package com.example.fantasy_trade_api.config.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FirebaseAuthFilter extends OncePerRequestFilter {

    private final FirebaseAuthenticationProvider authenticationProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String idToken = resolveToken(request);
        if (idToken == null || request.getRequestURI().startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }
        Authentication auth = authenticationProvider.createAuthentication(idToken);
        if (auth == null) {
            filterChain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token!=null && token.startsWith("Bearer ")) {
            return token.substring(7);
        } else {
            return null;
        }
    }
}
