package com.spvue.Orders.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CartItem {
    private String color;
    private String img;
    private String orderUser;
    private Integer price;
    private Integer quantity;
    private String size;
    private String title;
    private Integer totalPrice;

    // Getters and Setters
}