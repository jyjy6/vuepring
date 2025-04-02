package com.spvue.Sales;


import com.spvue.CustomUserDetails;
import com.spvue.Member.MemberRepository;
import com.spvue.Sales.Embeddable.Benefit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sales")
public class SalesController {
    private final SalesService salesService;
    private final MemberRepository memberRepository;

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody Sales sales,
                                                Authentication auth) {
        salesService.save(sales, auth);
        return ResponseEntity.ok("Post created");
    }

    @GetMapping("/allitems")
    public ResponseEntity<List<Sales>> getAllSales() {
        List<Sales> salesList = salesService.findAll();
        System.out.println(salesList);
        return ResponseEntity.ok(salesList);
    }

    @GetMapping("/items")
    public ResponseEntity<Map<String, Object>> getPaginatedSales(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size) {

        // 페이지 및 크기 정보로 데이터 조회
        Page<Sales> salesPage = salesService.findPaginated(page - 1, size);

        // 응답에 필요한 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("items", salesPage.getContent());
        response.put("currentPage", salesPage.getNumber() + 1);
        response.put("totalItems", salesPage.getTotalElements());
        response.put("totalPages", salesPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

//
//    List<SalesAllDTO> salesDTOList = salesList.stream()
//            .map(sale -> {
//                SalesAllDTO dto = new SalesAllDTO();
//                dto.setId(sale.getId());
//                dto.setName(sale.getName());
//                dto.setFileURLs(sale.getFileURLs());
//                dto.setPrice(sale.getPrice());
//                dto.setShipping(sale.getShipping());
//                dto.setManufacturer(sale.getManufacturer());
//                dto.setBrand(sale.getBrand());
//                dto.setCondition(sale.getCondition());
//                dto.setOrigin(sale.getOrigin());
//                dto.setCreatedAt(sale.getCreatedAt());
//                return dto;
//            })
//            .collect(Collectors.toList());



}
