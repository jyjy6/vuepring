package com.spvue.News;

import com.spvue.Image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NewsService {
    private final NewsRepository newsRepository;
    private final ImageService imageService;

    public void postNews(News news, Authentication auth){

        // 2. 현재 로그인한 사용자 정보 가져오기
        String username = auth.getName(); // 기본적으로 username(email 등)이 들어감
        // 3. 권한(Role) 가져오기
        String role = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("USER"); // 기본값 USER
        // 4. News 객체에 설정
        news.setAuthor(username);
        news.setRole(role);
        newsRepository.save(news);
        List<String> URLs = news.getFileURLs();
        for (String imgURL : URLs) {
            imageService.imageFinalSave(imgURL);  // 각 URL에 대해 imageFinalSave 호출
        }
    }
}
