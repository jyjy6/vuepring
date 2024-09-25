package com.spvue.Sales.Embeddable;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Discount {
    private String grade;
    private Integer discountRate;
}