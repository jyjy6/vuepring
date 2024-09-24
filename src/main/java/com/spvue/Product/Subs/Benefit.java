package com.spvue.Product.Subs;

import com.spvue.Product.ShopProduct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Benefit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;  // 혜택 설명 (ex: "최대 적립 포인트")
    private Integer amount;  // 혜택 금액 (ex 1%)

    @ManyToOne
    @JoinColumn(name = "shop_product_id")
    private ShopProduct shopProduct;  // 혜택이 적용되는 상품

    // Getter, Setter
}
