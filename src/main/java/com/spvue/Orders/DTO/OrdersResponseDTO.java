package com.spvue.Orders.DTO;

import com.spvue.Member.Member;
import com.spvue.Orders.Orders;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
public class OrdersResponseDTO {
    private Long id;
    private String orderer;
    private String title;
    private String color;
    private Integer price;
    private Integer quantity;
    private Integer totalPrice;
    private String address;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private Orders.DeliveryStatus deliveryStatus = Orders.DeliveryStatus.ORDER_COMPLETED;
    private String username;
}


