package com.example.template.controller;

import com.example.template.dto.ApiResponse;
import com.example.template.dto.call.request.ChatRequest;
import com.example.template.dto.response.ChatMessageResponse;
import com.example.template.entity.ChatMessage;
import com.example.template.service.ChatMessageService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {

    ChatMessageService chatService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getAllMessages() {
        List<ChatMessageResponse> messages = chatService.getAll();
        ApiResponse<List<ChatMessageResponse>> response = ApiResponse.<List<ChatMessageResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Get all chat messages successfully")
                .data(messages)
                .build();
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<ApiResponse<ChatMessage>> chat(@RequestBody ChatRequest request) {
        ChatMessage reply = chatService.chat(request);
        ApiResponse<ChatMessage> response = ApiResponse.<ChatMessage>builder()
                .code(HttpStatus.OK.value())
                .message("Chat successfully")
                .data(reply)
                .build();
        return ResponseEntity.ok(response);
    }
}
