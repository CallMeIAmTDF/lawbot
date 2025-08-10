package com.example.template.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "files")
public class File extends BaseEntity {
    @Id
    String id;

    String name;

    String url;

    @Column(columnDefinition = "LONGTEXT")
    String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileType fileType;

    String category;

    Integer size;

    Boolean deleted;

//    String hash;


    public enum FileType {
        PDF, TXT, DOCX
    }
}