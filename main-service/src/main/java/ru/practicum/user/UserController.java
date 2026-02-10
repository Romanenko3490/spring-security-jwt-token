package ru.practicum.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/welcome")
@Slf4j
public class UserController {

    //tests requests
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseMsg welcomeUser(Authentication authentication) {
        System.out.println("=== MAIN SERVICE CONTROLLER CALLED ===");
        System.out.println("Authentication: " + authentication);

        if (authentication == null) {
            System.out.println("ERROR: Authentication is null in Main Service!");
            return ResponseMsg.builder()
                    .message("ERROR: Not authenticated")
                    .build();
        }

        String username = authentication.getName();
        System.out.println("Username: " + username);

        return ResponseMsg.builder()
                .message("Welcome user " + username + " auth: " + authentication)
                .build();
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseMsg welcomeAdmin(Authentication authentication) {
        System.out.println("=== MAIN SERVICE CONTROLLER CALLED ===");
        System.out.println("Authentication: " + authentication);

        if (authentication == null) {
            System.out.println("ERROR: Authentication is null in Main Service!");
            return ResponseMsg.builder()
                    .message("ERROR: Not authenticated")
                    .build();
        }

        String username = authentication.getName();
        System.out.println("Username: " + username);

        return ResponseMsg.builder()
                .message("Welcome user " + username + " auth: " + authentication)
                .build();
    }


}
