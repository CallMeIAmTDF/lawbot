package com.example.template.config.filter;

import com.example.template.service.RateLimiterService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(2)
public class ChatRateLimitFilter implements Filter {
    private final RateLimiterService rateLimiterService;

    public ChatRateLimitFilter(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if ("POST".equalsIgnoreCase(httpRequest.getMethod()) && httpRequest.getRequestURI().startsWith("/chat")) {
            if (!rateLimiterService.isAllowed("chat")) {
                httpResponse.setHeader("Access-Control-Allow-Origin", httpRequest.getHeader("Origin"));
                httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
                httpResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
                httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

                httpResponse.setStatus(429);
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"code\":429,\"message\":\"Too many chat requests\"}");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}