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
    private boolean imgUsed = false;
//    Optional<Image> image = imageRepository.findByImageUrl(imgURL);
//
//    image가 null이아니면private boolean imgUsed = false;컬럼의 값을 1로 바꾸고 싶은데 어캐함

}
