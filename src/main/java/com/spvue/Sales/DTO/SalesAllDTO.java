package com.spvue.Sales.DTO;

import com.spvue.Member.Member;
import com.spvue.Sales.Embeddable.Benefit;
import com.spvue.Sales.Embeddable.Discount;
import com.spvue.Sales.Embeddable.Option;
import com.spvue.Sales.Embeddable.ShippingInfo;
import jakarta.persistence.ElementCollection;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesAllDTO {

        private Long id;
        private String name;
        private List<String> fileURLs;
        private Integer price;
        private String shipping;
        private String manufacturer;
        private String brand;
        private String condition;
        private String origin;
        private LocalDateTime createdAt;
        private String author;
        private List<Option> options;
        private List<Benefit> benefits;
        private List<ShippingInfo> shippingInfo;
        @ElementCollection
        private List<Discount> discounts;

}
