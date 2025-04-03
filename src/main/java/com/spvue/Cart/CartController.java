package com.spvue.Cart;

import com.spvue.Member.Member;
import com.spvue.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartDto>> getCartList(Authentication auth) {
        List<CartDto> cartList = cartService.cartList(auth);
        return ResponseEntity.ok(cartList);
    }

    @PostMapping("/insert")
    public ResponseEntity<String> saveCart(@RequestBody List<CartDto> cartDtos) {
        System.out.println("insert 요청됨");

        try {
            for (CartDto dto : cartDtos) {
                System.out.println("입력된 사용자명: " + dto.getUsername());

                // username으로 Member 조회
                Member member = memberRepository.findByUsername(dto.getUsername())
                        .orElseThrow(() -> new RuntimeException("Member not found"));

                System.out.println("조회된 멤버: " + member);

                // 기존에 같은 상품이 있는지 확인
                Optional<Cart> existingCart = cartRepository.findByMemberAndTitleAndColorAndSize(
                        member, dto.getTitle(), dto.getColor(), dto.getSize()
                );

                if (existingCart.isPresent()) {
                    // ✅ 기존 상품이 있으면 수량(quantity) 및 총 가격(totalPrice) 업데이트
                    Cart cart = existingCart.get();
                    cart.setQuantity(dto.getQuantity()); // 기존 수량에 추가
                    cart.setTotalPrice(dto.getTotalPrice()); // 기존 가격에 추가

                    cartRepository.save(cart); // 업데이트 저장
                    System.out.println("✅ 기존 상품 업데이트 완료: " + cart);
                } else {
                    // ✅ 기존 상품이 없으면 새로운 상품 저장
                    Cart cart = Cart.builder()
                            .title(dto.getTitle())
                            .color(dto.getColor())
                            .price(dto.getPrice())
                            .size(dto.getSize())
                            .img(dto.getImg())
                            .quantity(dto.getQuantity())
                            .totalPrice(dto.getTotalPrice())
                            .member(member)
                            .build();

                    cartRepository.save(cart); // 새로운 항목 저장
                    System.out.println("✅ 새로운 상품 저장 완료: " + cart);
                }
            }

            return ResponseEntity.ok("성공적으로 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("오류 발생: " + e.getMessage());
        }
    }



}
