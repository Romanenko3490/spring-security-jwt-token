package ru.practicum.security;

import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationAuthFilter extends CommonJwtAuthenticationFilter {

    public JwtAuthenticationAuthFilter(JwtServiceAuth jwtServiceAuth) {
        super(jwtServiceAuth);
    }
}