package com.spvue.Cart;

import com.spvue.Auth.OAuth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;

    public List<CartDto> cartList(Authentication auth) {
        String username = ((CustomUserDetails) auth.getPrincipal()).getUsername();
        List<Cart> cartList = cartRepository.findByMember_Username(username);

        // DTO 변환 후 반환
        return cartList.stream()
                .map(CartDto::fromEntity)
                .collect(Collectors.toList());
    }


    public void deleteCartItemById(Long id) {
        if (!cartRepository.existsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 장바구니 항목입니다.");
        }
        cartRepository.deleteById(id);
    }

}
