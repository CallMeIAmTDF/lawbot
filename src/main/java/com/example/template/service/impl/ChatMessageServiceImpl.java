package com.example.template.service.impl;


import com.example.template.advice.exception.BadRequestException;
import com.example.template.dto.call.request.ChatRequest;
import com.example.template.dto.call.response.ChatResponse;
import com.example.template.dto.call.response.Source;
import com.example.template.dto.mapper.ChatMapper;
import com.example.template.dto.response.ChatMessageResponse;
import com.example.template.entity.ChatMessage;
import com.example.template.entity.File;
import com.example.template.repository.ChatMessageRepository;
import com.example.template.repository.FileRepository;
import com.example.template.repository.UserRepository;
import com.example.template.service.ChatMessageService;
import com.example.template.service.call.RAGService;
import com.example.template.util.SecurityUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMessageServiceImpl implements ChatMessageService {
    ChatMessageRepository chatMessageRepository;
    SecurityUtil securityUtil;
    UserRepository userRepository;
    RAGService ragService;
    FileRepository fileRepository;
    ChatMapper chatMapper;

    @Override
    public List<ChatMessageResponse> getAll() {
        String userId = getUid();
        return chatMessageRepository.findAllByUserIdOrderByCreatedAtAsc(userId).stream().map(chatMapper::toChatMessageResponse).collect(Collectors.toList());
    }

    @Override
    public ChatMessage chat(ChatRequest request) {
        String userId = getUid();

        chatMessageRepository.save(ChatMessage.builder()
                .userId(userId)
                .content(request.getQuery())
                .role(ChatMessage.MessageRole.USER)
                .file(null)
                .page(0)
                .build());
        ChatMessage chatMessage;
        try{
            ChatResponse response = ragService.chat(request);
            List<Source> sources = response.getSources();
            File file = null;
            int page = 0;

            if (sources != null && !sources.isEmpty()) {
                Source source = sources.get(0);
                file = fileRepository.findFirstByUrl(source.getSource()).orElse(null);
                page = source.getPage() == null ? 0 : source.getPage();
            }
            chatMessage = ChatMessage.builder()
                    .userId(userId)
                    .content(response.getAnswer())
                    .role(ChatMessage.MessageRole.BOT)
                    .file(file)
                    .page(page)
                    .build();
        }catch (Exception e){
            chatMessage = ChatMessage.builder()
                    .userId(userId)
                    .content("Xin lỗi. Hệ thống gặp lỗi, vui lòng thử lại sau!!!")
                    .role(ChatMessage.MessageRole.BOT)
                    .file(null)
                    .page(0)
                    .build();
        }
        chatMessageRepository.save(chatMessage);
        return chatMessage;


    }

    private String getUid(){
        String email = securityUtil.getCurrentUserLogin().get();
        return userRepository.findByEmail(email).get().getId();
    }

}
