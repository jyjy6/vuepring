package com.spvue.Product;

import com.spvue.CustomUserDetails;
import com.spvue.Image.ImageService;
import com.spvue.Member.Member;
import com.spvue.Member.MemberRepository;
import com.spvue.News.News;
import com.spvue.Product.DTO.ProductRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.awscore.util.SignerOverrideUtils;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/products")
public class ShopProductController {
    private final ShopProductService shopProductService;
    private final MemberRepository memberRepository;

    @PostMapping("/write")
    public ResponseEntity<String> postNews(@RequestBody ProductRequestDTO productRequestDTO,
                                           Authentication auth) {
        System.out.println(auth.isAuthenticated());
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        String username = user.getUsername(); // 사용자 이름

        // 사용자 이름으로 Member 객체 조회
        Optional<Member> optionalAuthor = memberRepository.findByUsername(username); // repository를 통해 Member 조회

        if (optionalAuthor.isPresent()) {
            Member author = optionalAuthor.get(); // Optional에서 Member 객체 가져오기
            shopProductService.save(productRequestDTO, author); // Author를 직접 전달
            return ResponseEntity.ok("Post created");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Author not found");
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductRequestDTO> getProduct(@PathVariable Long id) {
        ShopProduct product = shopProductService.findById(id);




    }



}

