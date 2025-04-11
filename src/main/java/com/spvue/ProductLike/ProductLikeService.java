package com.spvue.ProductLike;

import com.spvue.Member.Member;
import com.spvue.Member.MemberRepository;
import com.spvue.Sales.DTO.SalesAllDTO;
import com.spvue.Sales.Sales;
import com.spvue.Sales.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductLikeService {

    private final MemberRepository memberRepository;
    private final SalesRepository salesRepository;
    private final ProductLikeRepository productLikeRepository;

    public void likeProduct(Long memberId, Long salesId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원 없음"));
        Sales sales = salesRepository.findById(salesId)
                .orElseThrow(() -> new RuntimeException("상품 없음"));

        // 중복 좋아요 체크
        Optional<ProductLike> existingLike = productLikeRepository.findByMemberAndSales(member, sales);

        if (existingLike.isPresent()) {
            // 이미 좋아요 누름 → 취소
            System.out.println("좋아요취소");
            productLikeRepository.delete(existingLike.get());
        } else {
            // 좋아요 등록
            System.out.println("좋아용");
            ProductLike like = ProductLike.builder()
                    .member(member)
                    .sales(sales)
                    .build();
            productLikeRepository.save(like);
        }
    }


    public List<SalesAllDTO> getLikedProducts(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원 없음"));

        List<ProductLike> likes = productLikeRepository.findAllByMember(member);

        return likes.stream()
                .map(ProductLike::getSales)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private SalesAllDTO convertToDTO(Sales sales) {
        return SalesAllDTO.builder()
                .id(sales.getId())
                .name(sales.getName())
                .fileURLs(sales.getFileURLs())
                .build();
    }

}
