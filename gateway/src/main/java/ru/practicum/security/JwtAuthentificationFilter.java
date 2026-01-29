package ru.practicum.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtAuthentificationFilter extends CommonJwtAuthenticationFilter {


    public JwtAuthentificationFilter(JwtService jwtService) {
        super(jwtService);
    }
}
