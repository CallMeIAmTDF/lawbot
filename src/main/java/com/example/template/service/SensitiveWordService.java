package com.example.template.service;

public interface SensitiveWordService {
    public boolean contains(String text);
    public String findMatchWord(String text);
}
