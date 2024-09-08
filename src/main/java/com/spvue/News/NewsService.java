package com.spvue.News;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NewsService {
    private final NewsRepository newsRepository;

    public void postNews(News news){
        newsRepository.save(news);
    }
}
