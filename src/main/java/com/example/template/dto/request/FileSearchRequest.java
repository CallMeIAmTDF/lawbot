package com.example.template.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileSearchRequest {
    private String keyword;
    private List<String> categories;
    private int page = 0;
    private int size = 10;
}