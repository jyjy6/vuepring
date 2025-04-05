package com.spvue.Cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Long id;
    private String title;
    private String color;
    private Integer price;
    private String size;
    private String img;
    private Integer quantity;
    private Integer totalPrice;
    private String username;  // 프론트랑 보내는 username (String)

//    프론트에서 get으로 받을떄 member통째로 받는현상이 일어나서 getMember().getUsername()으로 유저네임만 보내기위함
    public static CartDto fromEntity(Cart cart) {
        return new CartDto(
                cart.getId(),
                cart.getTitle(),
                cart.getColor(),
                cart.getPrice(),
                cart.getSize(),
                cart.getImg(),
                cart.getQuantity(),
                cart.getTotalPrice(),
                cart.getMember().getUsername() // Member 전체가 아닌 username만 가져옴
        );
    }
}


