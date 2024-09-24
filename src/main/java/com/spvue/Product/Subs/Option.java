package com.spvue.Product.Subs;

import com.spvue.Product.ShopProduct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_option") // 테이블 이름을 product_option으로 설정
@Getter
@Setter
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String size;  // 옵션 사이즈
    private String color;  // 옵션 색상
    private String other; // 다른옵션

    @ManyToOne
    @JoinColumn(name = "shop_product_id")
    private ShopProduct shopProduct;  // 상품과 관계

    // Getter, Setter
}
