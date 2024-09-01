package com.spvue.Image;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
public class Image {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    private String imageUrl;
    private String imageName;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private String role = "USER";


}
