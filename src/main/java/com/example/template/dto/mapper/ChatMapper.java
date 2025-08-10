package com.example.template.dto.mapper;

import com.example.template.dto.response.ChatMessageResponse;
import com.example.template.entity.ChatMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMapper{
    public ChatMessageResponse toChatMessageResponse(ChatMessage message){
        return ChatMessageResponse.builder()
                .id(message.getId())
                .userId(message.getUserId())
                .content(message.getContent())
                .role(message.getRole())
                .page(message.getPage())
                .file(message.getFile() != null ? new FileMapper().toFileResponse(message.getFile()) : null)
                .build();
    }
}
