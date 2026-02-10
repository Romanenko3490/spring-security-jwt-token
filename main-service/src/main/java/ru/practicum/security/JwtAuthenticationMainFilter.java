package ru.practicum.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtAuthenticationMainFilter extends CommonJwtAuthenticationFilter {
    public JwtAuthenticationMainFilter(JwtServiceMain jwtService) {
        super(jwtService);
    }
}
