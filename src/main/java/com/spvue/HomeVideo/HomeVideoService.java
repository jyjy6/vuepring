package com.spvue.HomeVideo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeVideoService {
    private final HomeVideoRepository homeVideoRepository;

    public void savePost(HomeVideo post) {
        homeVideoRepository.save(post);
    }
}
