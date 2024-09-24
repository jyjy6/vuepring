package com.spvue.Sales.Embeddable;

import jakarta.persistence.Embeddable;

@Embeddable
public class ShippingInfo {
    private String type;
    private Boolean freeShipping;
    private String courier;
    private Integer islandAdditionalFee;
}