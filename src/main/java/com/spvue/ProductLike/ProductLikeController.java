package com.spvue.ProductLike;

import com.spvue.Sales.DTO.SalesAllDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class ProductLikeController {

    private final ProductLikeService productLikeService;

    @PostMapping
    public ResponseEntity<?> likeProduct(@RequestBody ProductLikeDto dto) {
        productLikeService.likeProduct(dto.getMemberId(), dto.getSalesId());
        return ResponseEntity.ok("좋아요 성공");
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<SalesAllDTO>> getLikedProducts(@PathVariable Long memberId) {
        List<SalesAllDTO> likedProducts = productLikeService.getLikedProducts(memberId);
        return ResponseEntity.ok(likedProducts);
    }

}
