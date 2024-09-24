package com.spvue.Product.Subs.Repository;

import com.spvue.Product.Subs.Benefit;
import com.spvue.Product.Subs.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingRepository extends JpaRepository<Shipping, Long> {
}
