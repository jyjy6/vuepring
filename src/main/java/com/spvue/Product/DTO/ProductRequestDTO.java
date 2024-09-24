package com.spvue.Product.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductRequestDTO {
    private String name;
    private Integer price;
    private List<String> fileURLs;
    private String manufacturer;  // 제조사
    private String brand;  // 브랜드
    private String condition;  // 상품 상태 (신상품 등)
    private String shipping;
    private String origin;  // 원산지

    private List<OptionDTO> options;  // 사이즈, 색상 옵션
    private List<BenefitDTO> benefits;  // 적립 포인트 등 혜택 정보
    private List<ShippingDTO> shippingInfo;  // 배송 정보
    private List<DiscountDTO> discounts;  // 할인 정보

    // Getter, Setter
}