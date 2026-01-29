package ru.practicum.user;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.clients.AuthWebClient;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    final AuthWebClient authWebClient;

    @PostMapping("/register")
    public Mono<ResponseMsg> register(@RequestBody @Valid RegisterRequest request) {
        return authWebClient.register(request);
    }


    @PostMapping("/login")
    public Mono<ResponseMsg> login(@RequestBody LoginRequest request) {
        return authWebClient.login(request);
    }

    @PostMapping("/validate")
    public Mono<ResponseMsg> validateToken(@RequestHeader("Authorization") String authHeader) {
        return authWebClient.validateToken(authHeader);
    }


    //tests requests
    @GetMapping("/welcome")
    @PreAuthorize("hasRole('USER')")
    public ResponseMsg welcomeUser(Authentication authentication) {
        log.info("get request to /welcome");
        String username = authentication.getName();

        return ResponseMsg.builder()
                .message("Welcome user " + username + " auth: " + authentication)
                .build();
    }
}
