package ru.practicum.security;

import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFilter extends CommonJwtAuthenticationFilter {

    public JwtAuthenticationFilter(JwtService jwtService) {
        super(jwtService);
    }
}