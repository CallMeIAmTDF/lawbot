package com.example.template.dto.response;

import com.example.template.entity.ChatMessage;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageResponse {
    Long id;
    String userId;
    String content;
    Integer page;
    ChatMessage.MessageRole role;
    FileResponse file;
}
