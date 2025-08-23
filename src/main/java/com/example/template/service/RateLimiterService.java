package com.example.template.service;

public interface RateLimiterService {
    boolean isAllowed(String endpoint);
}
