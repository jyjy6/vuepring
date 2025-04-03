package com.spvue.Cart;

import com.spvue.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByMember_Username(String username);

    void deleteAllByMember_Username(String username);

    Optional<Cart> findByMemberAndTitleAndColorAndSize(Member member, String title, String color, String size);
}
