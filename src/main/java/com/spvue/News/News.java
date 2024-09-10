package com.spvue.News;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;


@Setter
@Getter
@ToString
@Entity
public class News {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> fileURLs; // 여러 파일 URL을 리스트로 받음

    private String title;
    @Lob
    private String content;
    private String author;
    private String role;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;


}
