package com.spvue.News;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Setter
@Getter
@ToString
@Entity
public class News {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String image;
    private String newsTitle;
    private String newsContent;
    private String author;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;



}
