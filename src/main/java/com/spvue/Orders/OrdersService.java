package com.spvue.Orders;


import com.spvue.Cart.CartRepository;
import com.spvue.Auth.OAuth.CustomUserDetails;
import com.spvue.Member.Member;
import com.spvue.Member.MemberRepository;
import com.spvue.Orders.DTO.CartItem;
import com.spvue.Orders.DTO.OrdersRequestDTO;
import com.spvue.Orders.DTO.OrdersResponseDTO;
import com.spvue.Sales.Sales;
import com.spvue.Sales.SalesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final SalesRepository salesRepository;

    public void updateOrderStatus(Long orderId, int statusCode) {

        // 주문을 ID로 조회
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order ID: " + orderId));
        // 상태 코드로부터 상태 업데이트
        order.updateDeliveryStatus(statusCode);

        // 변경된 상태로 주문 저장
        ordersRepository.save(order);

    }


    public List<OrdersResponseDTO> getAllOrdersDTO() {
        List<Orders> orders = ordersRepository.findAll();
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private OrdersResponseDTO convertToDTO(Orders order) {
        return OrdersResponseDTO.builder()
                .id(order.getId())
                .orderer(order.getOrderer())
                .title(order.getTitle())
                .color(order.getColor())
                .price(order.getPrice())
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .address(order.getAddress())
                .phoneNumber(order.getPhoneNumber())
                .createdAt(order.getCreatedAt())
                .deliveryStatus(order.getDeliveryStatus())
                .username(order.getUsername() != null ? order.getUsername().getUsername() : "비회원")
                .build();
    }


    @Transactional
    public void save(OrdersRequestDTO ordersRequestDTO, Authentication auth) {
        Long memberId = ((CustomUserDetails) auth.getPrincipal()).getId();
        Member user = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("그런유저 없음"));

        System.out.println(user);
        // orderRequest에서 필요한 정보 추출
        List<CartItem> cartItems = ordersRequestDTO.getCartItems();
        for (CartItem item : cartItems) {
            Orders order = new Orders();
            Sales salesModel = salesRepository.findByName(item.getTitle());
            String modelColor = Optional.ofNullable(salesModel.getOptions())
                    .filter(options -> !options.isEmpty()) // 리스트가 비어있지 않은지 확인
                    .map(options -> options.get(0).getColor()) // 첫 번째 옵션의 color 가져오기
                    .orElse("기본 색상"); // 값이 없으면 기본값 설정
            Integer modelPrice = Optional.ofNullable(salesModel.getPrice()).orElse(99999999);
            order.setOrderer(ordersRequestDTO.getOrderer());
            order.setAddress(ordersRequestDTO.getAddress());
            order.setPhoneNumber(ordersRequestDTO.getPhoneNumber());
            // 위 까지의 항목은 ordersRequestDTO에서 바로뽑아올 수 있지만
            // 아래의 항목은 List<CartItem>의 항목이라 따로 뽑아야함

            // modelPrice, modelColor항목을 따로 변수로 추출한 이유는
            // 악성유저가 프론트에서 가격, 색상등을 조작했을때 큰일날 수 있기때문에 서버수준에서 관리.
            order.setPrice(modelPrice);
            order.setColor(modelColor);
            order.setQuantity(item.getQuantity());
            order.setTotalPrice(modelPrice * item.getQuantity());
            order.setTitle(item.getTitle());
            order.setUsername(user);

            ordersRepository.save(order);
        }
        cartRepository.deleteAllByMember_Username(user.getUsername());
    }

    @Transactional
    public void saveForGuest(OrdersRequestDTO ordersRequestDTO) {
        // 비회원 주문 처리
        List<CartItem> cartItems = ordersRequestDTO.getCartItems();
        for (CartItem item : cartItems) {
            Sales salesModel = salesRepository.findByName(item.getTitle());
            Orders order = new Orders();
            Integer modelPrice = Optional.ofNullable(salesModel.getPrice()).orElse(99999999);
            String modelColor = Optional.ofNullable(salesModel.getOptions())
                    .filter(options -> !options.isEmpty()) // 리스트가 비어있지 않은지 확인
                    .map(options -> options.get(0).getColor()) // 첫 번째 옵션의 color 가져오기
                    .orElse("기본 색상"); // 값이 없으면 기본값 설정

            order.setOrderer(ordersRequestDTO.getOrderer());
            order.setAddress(ordersRequestDTO.getAddress());
            order.setPhoneNumber(ordersRequestDTO.getPhoneNumber());
            order.setPrice(modelPrice);
            order.setColor(modelColor);
            order.setTitle(item.getTitle());
            order.setQuantity(item.getQuantity());
            order.setTotalPrice(modelPrice * item.getQuantity());

            // 비회원의 경우 회원 정보는 null로 설정
            order.setUsername(null);

            // OrderService를 통해 개별 아이템 저장
            ordersRepository.save(order);
        }
    }


}
