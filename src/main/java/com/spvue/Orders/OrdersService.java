package com.spvue.Orders;


import com.spvue.CustomUserDetails;
import com.spvue.Member.Member;
import com.spvue.Member.MemberRepository;
import com.spvue.Orders.DTO.CartItem;
import com.spvue.Orders.DTO.OrdersRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final MemberRepository memberRepository;

    public void updateOrderStatus(Long orderId, int statusCode) {
        // 주문을 ID로 조회
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order ID: " + orderId));

        // 상태 코드로부터 상태 업데이트
        order.updateDeliveryStatus(statusCode);
        // 변경된 상태로 주문 저장
        ordersRepository.save(order);
    }

    public void save(OrdersRequestDTO ordersRequestDTO, Authentication auth) {
        Long memberId = ((CustomUserDetails)auth.getPrincipal()).getId();
        Member user = memberRepository.findById(memberId).orElseThrow(()-> new IllegalArgumentException("그런유저 없음"));
        System.out.println(user);
        // orderRequest에서 필요한 정보 추출
        List<CartItem> cartItems = ordersRequestDTO.getCartItems();
        for (CartItem item : cartItems) {
            Orders order = new Orders();
            order.setOrderer(ordersRequestDTO.getOrderer());
            order.setAddress(ordersRequestDTO.getAddress());
            order.setPhoneNumber(ordersRequestDTO.getPhoneNumber());
            order.setPrice(item.getPrice());
            order.setColor(item.getColor());
            order.setQuantity(item.getQuantity());
            order.setTotalPrice(item.getTotalPrice());
            order.setTitle(item.getTitle());
            order.setUsername(user);
            // 기타 필드 설정...

            // OrderService를 통해 개별 아이템 저장
            ordersRepository.save(order);
        }
    }

    public void saveForGuest(OrdersRequestDTO ordersRequestDTO) {
        // 비회원 주문 처리
        List<CartItem> cartItems = ordersRequestDTO.getCartItems();
        for (CartItem item : cartItems) {
            Orders order = new Orders();
            order.setOrderer(ordersRequestDTO.getOrderer());
            order.setAddress(ordersRequestDTO.getAddress());
            order.setPhoneNumber(ordersRequestDTO.getPhoneNumber());
            order.setPrice(item.getPrice());
            order.setColor(item.getColor());
            order.setTitle(item.getTitle());
            order.setQuantity(item.getQuantity());
            order.setTotalPrice(item.getTotalPrice());

            // 비회원의 경우 회원 정보는 null로 설정
            order.setUsername(null);

            // OrderService를 통해 개별 아이템 저장
            ordersRepository.save(order);
        }
    }


}
