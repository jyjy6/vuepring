package com.spvue.Sales.Embeddable;

import jakarta.persistence.Embeddable;

@Embeddable
public class Option {
    private String size;
    private String color;
    private String other;
}
