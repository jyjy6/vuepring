package com.spvue.News;

import com.spvue.Boxer.Boxer;
import com.spvue.Image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/news")
public class NewsController {
    private final NewsRepository newsRepository;
    private final NewsService newsService;
    private final ImageService imageService;

    @PostMapping("/post")
    public ResponseEntity<String> postNews(@RequestBody News news, Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        newsService.postNews(news, auth);

        return ResponseEntity.ok("Post created");
    }


    @GetMapping("/data")
    public List<News> allNewsData(){
        List<News> result = newsRepository.findAll();
        //내림차순 정렬
        result.sort(Comparator.comparingLong(News::getId).reversed());
        return result;
    }
}
