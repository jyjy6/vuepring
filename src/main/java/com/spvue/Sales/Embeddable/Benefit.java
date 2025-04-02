package com.spvue.Sales.Embeddable;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Embeddable
public class Benefit {
    @Column(name = "description")
    private String description;
    @Column(name = "amount")
    private Integer amount;
}