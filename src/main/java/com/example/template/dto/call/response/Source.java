package com.example.template.dto.call.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Source {
    String source;
    Integer page;
    Double similarity;
    Double rerank_score;
}