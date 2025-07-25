package com.spvue.HomeVideo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@ToString
public class HomeVideo {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String title;
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;
    private String link;
    private String author;
    @ElementCollection
    private List<String> fileURLs; // 여러 파일 URL을 리스트로 받음

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public HomeVideo() {
        this.createdAt = LocalDateTime.now(); // 생성 시 현재 시간 설정
    }
}
