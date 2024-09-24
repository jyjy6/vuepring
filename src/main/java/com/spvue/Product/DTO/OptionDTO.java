package com.spvue.Product.DTO;


import com.spvue.Product.ShopProduct;
import com.spvue.Product.Subs.Option;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class OptionDTO {
    private String size;
    private String color;
    private String other; // 다른옵션

    // Getter, Setter

}