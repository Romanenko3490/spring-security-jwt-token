package ru.practicum.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.practicum.clients.UserClient;
import ru.practicum.security.JwtServiceGetaway;

@RestController
@RequestMapping("/welcome")
@Slf4j
@RequiredArgsConstructor
public class WelcomeController {

    private final UserClient userClient;
    private final JwtServiceGetaway jwtServiceGetaway;

    //tests requests
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseMsg welcomeUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        System.out.println("=== WELCOME CONTROLLER CALLED! ===");
        System.out.println("Auth header: " + authorizationHeader);

        // Проверяем SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("SecurityContext Authentication: " + auth);
        System.out.println("Is authenticated: " + (auth != null && auth.isAuthenticated()));
        if (auth != null) {
            System.out.println("Authorities: " + auth.getAuthorities());
        }

        String token = authorizationHeader.substring(7);
        String role = jwtServiceGetaway.extractRole(token);
        System.out.println("Extracted role from token: " + role);  // Должно быть "USER"
        System.out.println("Creating authority: ROLE_" + role);    // Должно быть "ROLE_USER"

        return userClient.welcomeUser(authorizationHeader);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseMsg welcomeAdmin(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        System.out.println("=== WELCOME CONTROLLER CALLED! ===");
        System.out.println("Auth header: " + authorizationHeader);

        // Проверяем SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("SecurityContext Authentication: " + auth);
        System.out.println("Is authenticated: " + (auth != null && auth.isAuthenticated()));
        if (auth != null) {
            System.out.println("Authorities: " + auth.getAuthorities());
        }

        String token = authorizationHeader.substring(7);
        String role = jwtServiceGetaway.extractRole(token);
        System.out.println("Extracted role from token: " + role);  // Должно быть "USER"
        System.out.println("Creating authority: ROLE_" + role);    // Должно быть "ROLE_USER"

        return userClient.welcomeAdmin(authorizationHeader);
    }
}
