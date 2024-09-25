package com.spvue.Sales.Embeddable;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class ShippingInfo {
    private String type;
    private Boolean freeShipping;
    private String courier;
    private Integer islandAdditionalFee;
}