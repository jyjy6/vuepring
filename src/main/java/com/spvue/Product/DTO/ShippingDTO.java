package com.spvue.Product.DTO;


import com.spvue.Product.ShopProduct;
import com.spvue.Product.Subs.Shipping;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingDTO {
    private String type;
    private Boolean freeShipping;
    private String courier;
    private Integer islandAdditionalFee;

    // Getter, Setter


}
