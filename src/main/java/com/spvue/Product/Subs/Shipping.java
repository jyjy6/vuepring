package com.spvue.Product.Subs;

import com.spvue.Product.ShopProduct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;  // 배송 종류 (ex: "택배배송")
    private Boolean freeShipping;  // 무료 배송 여부
    private String courier;  // 택배사 (ex: "우체국택배")
    private Integer islandAdditionalFee;  // 제주/도서산간 추가 요금

    @ManyToOne
    @JoinColumn(name = "shop_product_id")
    private ShopProduct shopProduct;  // 상품과 관계

    // Getter, Setter
}
