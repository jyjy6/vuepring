package com.spvue.Orders;

import com.spvue.Member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder
@ToString
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderer;
    private String title;
    private String color;
    private Integer price;
    private Integer quantity;
    private Integer totalPrice;
    private String address;
    private String phoneNumber;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(
            name="member_username",
            referencedColumnName = "username", // username 컬럼을 참조
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member username;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus = DeliveryStatus.ORDER_COMPLETED;



    // 상태 코드로부터 상태를 업데이트하는 메서드
    public void updateDeliveryStatus(int statusCode) {
        this.deliveryStatus = DeliveryStatus.fromCode(statusCode);
    }

    //배송상태 enum
    public enum DeliveryStatus {
        ORDER_COMPLETED(1),     // 주문 완료
        PAYMENT_COMPLETED(2),   // 결제 완료
        SHIPPING_STARTED(3),    // 배송 시작
        SHIPPING_COMPLETED(4),  // 배송 완료
        RETURN_IN_PROGRESS(5),  // 반품 진행
        RETURN_COMPLETED(6),    // 반품 완료
        PURCHASE_COMPLETED(7);    // 반품 완료

        private final int code;

        DeliveryStatus(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        // 숫자로부터 enum 값으로 변환하는 메서드
        public static DeliveryStatus fromCode(int code) {
            for (DeliveryStatus status : DeliveryStatus.values()) {
                if (status.getCode() == code) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid code for DeliveryStatus: " + code);
        }
    }


}



