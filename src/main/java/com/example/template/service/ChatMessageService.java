package com.example.template.service;

import com.example.template.dto.call.request.ChatRequest;
import com.example.template.dto.response.ChatMessageResponse;
import com.example.template.entity.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    List<ChatMessageResponse> getAll();
    ChatMessage chat(ChatRequest request);
}
