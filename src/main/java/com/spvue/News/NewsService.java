package com.spvue.News;

import com.spvue.Image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NewsService {
    private final NewsRepository newsRepository;
    private final ImageService imageService;

    public void postNews(News news){
        newsRepository.save(news);
        List<String> URLs = news.getFileURLs();
        for (String imgURL : URLs) {
            imageService.imageFinalSave(imgURL);  // 각 URL에 대해 imageFinalSave 호출
        }
    }
}
