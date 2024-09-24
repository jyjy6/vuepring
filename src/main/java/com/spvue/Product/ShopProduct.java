package com.spvue.Product;


import com.spvue.Member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;


@Setter
@Getter
@Entity
public class ShopProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;  // 상품 이름
    @ElementCollection
    private List<String> fileURLs; // 여러 파일 URL을 리스트로 받음
    private Integer price;  // 가격
    private String shipping;  // 배송 정보
    private String manufacturer;  // 제조사
    private String brand;  // 브랜드
    @Column(name = "`condition`")
    private String condition;
    private String origin;  // 원산지


    @CreationTimestamp
    private LocalDateTime createdAt;  // 생성일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member author;  // 작성자

    // Getter, Setter


}
