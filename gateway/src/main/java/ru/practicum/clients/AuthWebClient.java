package ru.practicum.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Mono;
import ru.practicum.user.LoginRequest;
import ru.practicum.user.RegisterRequest;
import ru.practicum.user.ResponseMsg;


@Service
@Slf4j
public class AuthWebClient extends BaseWebClient {
    private static final String API_PREFIX = "/auth";

    public AuthWebClient(@Value("${auth-service.url}") String baseUrl) {
        super(baseUrl, API_PREFIX);
    }

    public Mono<ResponseMsg> register(@RequestBody RegisterRequest request) {
        log.info("Register request {}", request);
        return webClient.post()
                .uri("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ResponseMsg.class)
                .doOnSuccess(response -> {
                    log.info("registered Successfully");
                })
                .doOnError(error -> log.error("Register failed"));
    }

    public Mono<ResponseMsg> login(@RequestBody LoginRequest request) {
        log.info("Login request {}", request);
        return webClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ResponseMsg.class)
                .doOnSuccess(response -> {
                    log.info("logged in successfully");
                })
                .doOnError(response -> {
                    log.info("Error due to log in");
                });
    }

    public Mono<ResponseMsg> validateToken(@RequestHeader("Authorization") String authHeader) {
        log.info("Validate token as header: " + authHeader);
        return webClient.post()
                .uri("/validate")
                .header("Authorization", authHeader)
                .retrieve()
                .bodyToMono(ResponseMsg.class)
                .doOnSuccess(response -> {
                    log.info("Token validated successfully");
                })
                .doOnError(response -> {
                    log.info("Token validation error");
                });
    }

}
