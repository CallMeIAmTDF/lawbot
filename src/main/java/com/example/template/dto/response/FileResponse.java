package com.example.template.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileResponse {
    String id;
    String name;
    String category;
    String description;
    LocalDateTime createdAt;
    Integer size;
    String url;
}
