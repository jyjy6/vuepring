package com.spvue.Orders;


import com.spvue.Orders.DTO.OrdersRequestDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrdersController {
    private final OrdersService ordersService;

    @Transactional
    @PostMapping
    public ResponseEntity<String> saveOrder(@RequestBody OrdersRequestDTO ordersRequestDTO,
                                            Authentication auth) {
        try {
            // auth가 null일 경우 비회원 주문 처리
            if (auth == null) {
                ordersService.saveForGuest(ordersRequestDTO);
            } else {
                ordersService.save(ordersRequestDTO, auth);
            }
            return ResponseEntity.ok("Order placed successfully");
        } catch (Exception e) {
            // 그 외의 모든 예외
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }


    @PostMapping("/{orderId}/update-status")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam("status") int status) {
        try {
            // 주문을 조회하고 상태 업데이트
            ordersService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok("Order status updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status code: " + status);
        }
    }

}
