package com.spvue.Sales.Embeddable;

import jakarta.persistence.Embeddable;

@Embeddable
public class Discount {
    private String grade;
    private Integer discountRate;
}