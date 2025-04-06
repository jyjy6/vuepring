package com.spvue.HomeVideo;

import com.spvue.Auth.OAuth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeVideoService {
    private final HomeVideoRepository homeVideoRepository;

    public void savePost(HomeVideo post, Authentication auth) {
        CustomUserDetails user = (CustomUserDetails)auth.getPrincipal();
        post.setAuthor(user.getUsername());
        homeVideoRepository.save(post);
    }
}
