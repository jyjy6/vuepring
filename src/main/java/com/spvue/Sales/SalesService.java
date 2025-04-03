package com.spvue.Sales;

import com.spvue.CustomUserDetails;
import com.spvue.Image.ImageService;
import com.spvue.Member.Member;
import com.spvue.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class SalesService {
    private final SalesRepository salesRepository;
    private final ImageService imageService;
    private final MemberRepository memberRepository;


    public void save(Sales sales,
                     Authentication auth) {
        CustomUserDetails user = (CustomUserDetails)auth.getPrincipal();
        Member member = memberRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Value not found!"));
        sales.setAuthor(member);


        salesRepository.save(sales);
        imageService.imageFinalSave(sales.getFileURLs().toArray(new String[0]));
    }

    public List<Sales> findAll() {
        return salesRepository.findAll();
    }


    public Page<Sales> findPaginated(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return salesRepository.findAll(pageRequest);
    }
}
