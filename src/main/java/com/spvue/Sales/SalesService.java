package com.spvue.Sales;

import com.spvue.CustomUserDetails;
import com.spvue.Image.ImageService;
import com.spvue.Member.Member;
import com.spvue.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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
        List<String> URLs = sales.getFileURLs();
        for (String imgURL : URLs) {
            imageService.imageFinalSave(imgURL);  // 각 URL에 대해 imageFinalSave 호출
        }
    }
}
