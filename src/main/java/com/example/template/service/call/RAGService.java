package com.example.template.service.call;

import com.example.template.dto.call.request.ChatRequest;
import com.example.template.dto.call.request.FileUploadRequest;
import com.example.template.dto.call.response.ChatResponse;
import com.example.template.dto.call.response.FileUploadResponse;

import java.io.IOException;

public interface RAGService {
    FileUploadResponse upload(FileUploadRequest request) throws IOException;

    ChatResponse chat(ChatRequest request);

    byte[] getFile(String url);
}
