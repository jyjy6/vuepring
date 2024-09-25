package com.spvue.Sales.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
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
        private String authorName; // 필요한 정보만 포함

        // Getters and Setters
}
