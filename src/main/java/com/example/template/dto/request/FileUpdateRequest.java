package com.example.template.dto.request;

import com.example.template.entity.File;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileUpdateRequest {
    private String name;
    private String description;
    private String category;
}