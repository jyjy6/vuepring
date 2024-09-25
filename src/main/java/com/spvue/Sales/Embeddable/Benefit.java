package com.spvue.Sales.Embeddable;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Embeddable
public class Benefit {
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "amount", nullable = false)
    private Integer amount;
}