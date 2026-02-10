package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.practicum.security.JwtServiceAuth;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final JwtServiceAuth jwtServiceAuth;
    private final PasswordEncoder passwordEncoder;


    //Регистрация юзер
    @PostMapping("/register/user")
    public ResponseMsg registerUser(@RequestBody RegisterRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) //важно
                .role(Roles.USER)
                .build();

        userService.save(user);

        return ResponseMsg.builder()
                .message("User successfully registered with id: " + user.getId())
                .build();
    }

    //Регистрация админ
    @PostMapping("/register/admin")
    public ResponseMsg registerAdmin(@RequestBody RegisterRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) //важно
                .role(Roles.ADMIN)
                .build();

        userService.save(user);

        return ResponseMsg.builder()
                .message("User successfully registered with id: " + user.getId())
                .build();
    }



    //логин
    @PostMapping("/login")
    public ResponseMsg login(@RequestBody LoginRequest request) {
        log.info("=== LOGIN ATTEMPT ===");
        log.info("Email: {}", request.getEmail());
        log.info("Password length: {}", request.getPassword() != null ? request.getPassword().length() : "null");
        try {

            User user = userService.findByEmail(request.getEmail());
            log.info("User found: {} (ID: {})", user.getUsername(), user.getId());
            log.info("DB password hash: {}", user.getPassword().substring(0, Math.min(30, user.getPassword().length())) + "...");


            boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
            log.info("Password matches: {}", passwordMatches);

            if (!passwordMatches) {
                log.warn("Password mismatch!");
                return ResponseMsg.builder()
                        .message("Logging error: Password mismatch!")
                        .build();
            }

            log.info("Authentication successful!");

            //генерируем токен
            String token = jwtServiceAuth.generateToken(
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole()
            );

            log.info("Token generated (first 30 chars): {}...", token.substring(0, Math.min(30, token.length())));

            //Возвращаем токен клиенту
            // 4. Возвращаем ответ
            Map<String, Object> response = new HashMap<>();
            response.put("access_token", token);
            response.put("token_type", "Bearer");
            response.put("expires_in", 86400);

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("email", user.getEmail());
            userInfo.put("role", user.getRole().name());
            response.put("user", userInfo);

            log.info("Login successful for user: {}", user.getUsername());

            return ResponseMsg.builder()
                    .message("Logging successfully: " + response)
                    .build();
        } catch (Exception e) {
            log.error("=== LOGIN ERROR ===");
            log.error("Error type: {}", e.getClass().getName());
            log.error("Error message: {}", e.getMessage());
            log.error("Stack trace:", e);

            return ResponseMsg.builder()
                    .message("some error message: " + e.getMessage())
                    .build();

        }
    }

    //Валидация токена
    @PostMapping("/validate")
    public ResponseMsg validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return ResponseMsg.builder()
                    .message("Invalid Header")
                    .build();
        }

        String token = authHeader.substring(7);
        boolean isValid = jwtServiceAuth.validateToken(token);

        if (!isValid) {
            return ResponseMsg.builder()
                    .message("Invalid Token")
                    .build();
        }

        String username = jwtServiceAuth.extractUsername(token);
        String role = jwtServiceAuth.extractRole(token);

        Map<String, Object> response = new HashMap<>();
        response.put("valid", true);
        response.put("username", username);
        response.put("role", role);
        response.put("message", "Token is valid");

        return ResponseMsg.builder()
                .message("Token is ok: " + response)
                .build();
    }

}
