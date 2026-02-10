package ru.practicum.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ConfigTestController {

    @Value("${main-service.url}")
    private String mainServiceUrl;

    @Value("${auth-service.url}")
    private String authServiceUrl;

    @GetMapping("/test-config")
    public Map<String, Object> testConfig() {
        System.out.println("=== CONFIG TEST CONTROLLER CALLED ===");
        System.out.println("mainServiceUrl = '" + mainServiceUrl + "'");
        System.out.println("authServiceUrl = '" + authServiceUrl + "'");

        try {
            Map<String, Object> response = Map.of(
                    "mainServiceUrl", mainServiceUrl,
                    "authServiceUrl", authServiceUrl,
                    "mainStartsWith//", mainServiceUrl.startsWith("//"),
                    "authStartsWith//", authServiceUrl.startsWith("//"),
                    "mainServiceUrl length", mainServiceUrl.length(),
                    "authServiceUrl length", authServiceUrl.length()
            );

            System.out.println("Response: " + response);
            return response;

        } catch (Exception e) {
            System.out.println("ERROR in controller: " + e.getMessage());
            e.printStackTrace();
            return Map.of("error", e.getMessage());
        }
    }

}
