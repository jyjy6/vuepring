package com.spvue.Cart;

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
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String color;
    private Integer price;
    private String size;
    private Integer quantity;
    private Integer totalPrice;
    private String img;
    @ManyToOne
    @JoinColumn(
            name="member_username",
            referencedColumnName = "username", // username 컬럼을 참조
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;




}



