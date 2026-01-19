package com.example.assignment.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int LIMIT = 100;
    private static final long WINDOW_MS = 15 * 60 * 1000; 

    private final Map<String, Window> cache = new ConcurrentHashMap<>();

    private static class Window {
        AtomicInteger count = new AtomicInteger(0);
        long startTime = Instant.now().toEpochMilli();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/v1/auth");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        long now = Instant.now().toEpochMilli();

        Window window = cache.computeIfAbsent(ip, k -> new Window());

        synchronized (window) {
            if (now - window.startTime > WINDOW_MS) {
                window.startTime = now;
                window.count.set(0);
            }

            if (window.count.incrementAndGet() > LIMIT) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Too many requests. Try again later.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
