package com.example.template.dto.mapper;

import com.example.template.dto.response.FileResponse;
import com.example.template.dto.response.FileStatsResponse;
import com.example.template.entity.File;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileMapper {
    public FileResponse toFileResponse(File file){
        return FileResponse.builder()
                .id(file.getId())
                .name(file.getName())
                .category(file.getCategory())
                .description(file.getDescription())
                .size(file.getSize())
                .url(file.getUrl())
                .createdAt(file.getCreatedAt())
                .build();
    }

    public FileStatsResponse toFileStatsResponse(File file){
        return FileStatsResponse.builder()
                .id(file.getId())
                .name(file.getName())
                .category(file.getCategory())
                .description(file.getDescription())
                .size(file.getSize())
                .url(file.getUrl())
                .createdAt(file.getCreatedAt())
                .deleted(file.getDeleted())
                .build();
    }
}
