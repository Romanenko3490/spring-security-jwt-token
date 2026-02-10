package ru.practicum.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtAuthentificationGetawayFilter extends CommonJwtAuthenticationFilter {


    public JwtAuthentificationGetawayFilter(JwtServiceGetaway jwtServiceGetaway) {
        super(jwtServiceGetaway);
    }
}
