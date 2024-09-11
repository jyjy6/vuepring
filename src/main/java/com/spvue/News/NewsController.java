package com.spvue.News;

import com.spvue.Boxer.Boxer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/news")
public class NewsController {
    private final NewsRepository newsRepository;


    @PostMapping("/post")
    public ResponseEntity<String> postNews(@RequestBody News news){
        newsRepository.save(news);
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
