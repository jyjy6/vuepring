package com.spvue.Orders.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class OrdersRequestDTO {
    private String orderer;
    private String address;
    private String phoneNumber;
    private List<CartItem> cartItems;

    // Getters and Setters
}
