package com.spvue.Sales.Embeddable;


import jakarta.persistence.Embeddable;

@Embeddable
public class Benefit {
    private String description;
    private Integer amount;
}