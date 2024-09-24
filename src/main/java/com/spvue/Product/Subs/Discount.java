package com.spvue.Product.Subs;

import com.spvue.Product.ShopProduct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String grade;  // 등급명 (ex: "VVIP", "VIP", "GOLD")
    private Integer discountRate;  // 중복 할인율 (ex: 3%)
    private Integer minQuantityForDiscount;  // 복수 구매 할인에 필요한 최소 수량

    @ManyToOne
    @JoinColumn(name = "shop_product_id")
    private ShopProduct shopProduct;  // 상품과 관계

    // Getter, Setter
}
