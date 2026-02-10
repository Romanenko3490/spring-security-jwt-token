package ru.practicum.clients;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.user.ResponseMsg;

@Service
@Slf4j
public class UserClient {

    private final static String API_PREFIX = "/welcome";

    private final RestTemplate restTemplate;
    private String baseUrl;

    public UserClient(@Value("${main-service.url}") String baseUrl) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl + API_PREFIX;
    }

    public ResponseMsg welcomeUser(String authorizationHeader) {
        String url = baseUrl + "/user";

        log.info("=== USER CLIENT CALLED ===");
        log.info("URL: {}", url);
        log.info("Token to forward: {}",
                authorizationHeader != null ?
                        authorizationHeader.substring(0, Math.min(30, authorizationHeader.length()))
                                + "..." : "NULL");

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", authorizationHeader);

            HttpEntity<String> request = new HttpEntity<>(headers);

            ResponseEntity<ResponseMsg> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    ResponseMsg.class);

            log.info("Success from Main Service: {}", response.getBody());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("Main Service returned error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Main Service error: " + e.getStatusCode(), e);
        } catch (Exception e) {
            log.error("Error calling Main Service: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to call Main Service", e);
        }

    }


    public ResponseMsg welcomeAdmin(String authorizationHeader) {
        String url = baseUrl + "/admin";

        log.info("=== USER CLIENT CALLED ===");
        log.info("URL: {}", url);
        log.info("Token to forward: {}",
                authorizationHeader != null ?
                        authorizationHeader.substring(0, Math.min(30, authorizationHeader.length()))
                                + "..." : "NULL");

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", authorizationHeader);

            HttpEntity<String> request = new HttpEntity<>(headers);

            ResponseEntity<ResponseMsg> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    ResponseMsg.class);

            log.info("Success from Main Service: {}", response.getBody());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("Main Service returned error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Main Service error: " + e.getStatusCode(), e);
        } catch (Exception e) {
            log.error("Error calling Main Service: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to call Main Service", e);
        }

    }
}
