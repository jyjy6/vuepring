package com.spvue.Sales.Embeddable;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Option {
    private String size;
    private String color;
    private String other;
}

