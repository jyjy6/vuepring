package com.spvue.Image;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;


    @ResponseBody
    @GetMapping("/presigned-url")
    public PresignedUrlResponse getImgUrl(@RequestParam String filename,
                                          @RequestParam String role){
        String decodedFilename = URLDecoder.decode(filename, StandardCharsets.UTF_8);
        var decodedRandomFilename = UUID.randomUUID().toString() + "_" + decodedFilename;
        var fullPath = "test/" + decodedRandomFilename;
        var presignedUrl = imageService.createPresignedUrl(fullPath);

        // 베이스 URL 생성 (쿼리 파라미터 없는 실제 이미지 URL)
        String imageUrl = "https://" + bucket + ".s3.amazonaws.com/" + fullPath;

        // 이미지 정보 저장
        imageService.saveImageInfo(imageUrl, decodedFilename, role);

        // 프론트엔드로 둘 다 전송
        return new PresignedUrlResponse(presignedUrl, imageUrl);
    }

}


@Getter
@Setter
@AllArgsConstructor
class PresignedUrlResponse {
    private String presignedUrl;
    private String imageUrl;

    // 생성자, getter, setter
}