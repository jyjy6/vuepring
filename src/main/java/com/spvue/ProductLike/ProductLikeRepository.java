package com.spvue.ProductLike;

import com.spvue.Member.Member;
import com.spvue.Sales.Sales;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {
    boolean existsByMemberAndSales(Member member, Sales sales);

    Optional<ProductLike> findByMemberAndSales(Member member, Sales sales);

    List<ProductLike> findAllByMember(Member member);
}
