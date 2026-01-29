package ru.practicum.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class CommonJwtAuthenticationFilter extends OncePerRequestFilter {

    private final CommonJwtService jwtService;

    public CommonJwtAuthenticationFilter(CommonJwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        //основная логика фильтра, выполняется только для защищенных путей

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendError(response, 401, "Not token provided");
            return;
        }

        String token = authHeader.substring(7);

        //Валидируем токен
        if (!jwtService.validateToken(token)) {
            sendError(response, 401, "Invalid token");
            return;
        }

        //Создаем аутентификацию
        try {
            String username = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    username, null, Collections.singleton(authority)
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (Exception e) {
            sendError(response, 401, "Token process error: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();

        System.out.println("=== Filter Debug ===");
        System.out.println("ServletPath: " + servletPath);
        System.out.println("RequestURI: " + requestURI);
        System.out.println("ContextPath: " + contextPath);
        System.out.println("Method: " + request.getMethod());
        System.out.println("=== End Debug ===");

        return requestURI.contains("/auth/register") ||
                requestURI.contains("/auth/login") ||
                requestURI.contains("/actuator/");

    }

    private void sendError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format(
                "{\"error\": \"%s\", \"status\": %d, \"timestamp\": \"%s\"}",
                message, status, java.time.Instant.now()
        ));
    }
}
