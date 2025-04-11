package com.spvue.ProductLike;


import com.spvue.Member.Member;
import com.spvue.Sales.Sales;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "sales_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 좋아요 누른 회원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 좋아요 누른 상품
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_id", nullable = false)
    private Sales sales;

}