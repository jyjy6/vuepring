package com.spvue.P4P;

import com.spvue.Boxer.Boxer;
import com.spvue.Boxer.BoxerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/p4p")
@RequiredArgsConstructor
public class P4PController {

    private final P4PService p4pService;
    private final BoxerRepository boxerRepository;
    private final P4PRepository p4pRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<?> updateP4PRanking(@RequestBody P4PDto p4pDto) {
        Boxer boxer = boxerRepository.findById(p4pDto.getBoxerId())
                .orElseThrow(() -> new EntityNotFoundException("Boxer not found with id: " + p4pDto.getBoxerId()));


        // 랭킹 서비스 호출하여 랭킹 업데이트
        p4pService.updateBoxerRanking(boxer, p4pDto.getP4pScore(), p4pDto.getP4pRanking());

        return ResponseEntity.ok("P4P 랭킹이 업데이트되었습니다.");
    }

    @GetMapping
    public List<P4P> allP4P(){
        Pageable pageable = PageRequest.of(0, 10); // 첫 번째 페이지에서 10개의 행을 가져옴
        List<P4P> result = p4pRepository.findAll();

        result.sort(Comparator.comparingInt(P4P::getP4pRanking)); // p4pRanking으로 오름차순 정렬
        return result;


    }



}
