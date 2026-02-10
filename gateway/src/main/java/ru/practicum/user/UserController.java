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
import ru.practicum.clients.UserClient;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final AuthWebClient authWebClient;
    private final UserClient userClient;

    @PostMapping("/register/user")
    public Mono<ResponseMsg> registerUser(@RequestBody @Valid RegisterRequest request) {
        return authWebClient.registerUser(request);
    }

    @PostMapping("/register/admin")
    public Mono<ResponseMsg> registerAdmin(@RequestBody @Valid RegisterRequest request) {
        return authWebClient.registerAdmin(request);
    }


    @PostMapping("/login")
    public Mono<ResponseMsg> login(@RequestBody LoginRequest request) {
        return authWebClient.login(request);
    }

    @PostMapping("/validate")
    public Mono<ResponseMsg> validateToken(@RequestHeader("Authorization") String authHeader) {
        return authWebClient.validateToken(authHeader);
    }

}
