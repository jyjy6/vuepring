package com.spvue.Image;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ImageCleanupService {

    @Value("${spring.cloud.aws.s3.bucket}")
    String bucket;
    private final ImageRepository imageRepository;
    private final S3Client s3Client;
    private final ImageService imageService;

    // 매 시간마다 불필요한 파일 삭제 작업 실행
    @Scheduled(fixedRate = 30000)
    public void cleanupUnusedImages() {
        // 일정 시간이 지나도 사용되지 않은 이미지를 조회
        List<Image> unusedImages = imageRepository.findByImgUsedFalse();


        for (Image image : unusedImages) {
            // S3에서 파일 삭제
            String s3Key = imageService.extractS3Key(image.getImageUrl());

            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(s3Key)
                    .build());

            // DB에서 파일 정보 삭제
            imageRepository.delete(image);
        }
    }
}