package com.spvue.Product.DTO;

import com.spvue.Product.ShopProduct;
import com.spvue.Product.Subs.Discount;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class DiscountDTO {
    private String grade;
    private Integer discountRate;



}