package com.example.template.service;

import com.example.template.dto.request.FileCreationRequest;
import com.example.template.dto.request.FileSearchRequest;
import com.example.template.dto.request.FileUpdateRequest;
import com.example.template.dto.response.FileResponse;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface FileService {
    void saveFile(FileCreationRequest request) throws IOException;

    Page<FileResponse> getFiles(FileSearchRequest request);

    void deleteFile(String id);

    FileResponse updateFile(String id, FileUpdateRequest request);

    byte[] getFile(String url);
}
