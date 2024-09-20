package com.spvue.HomeVideo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@ToString
public class HomeVideo {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String content;
    private String link;
    private String author;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public HomeVideo() {
        this.createdAt = LocalDateTime.now(); // 생성 시 현재 시간 설정
    }
}
