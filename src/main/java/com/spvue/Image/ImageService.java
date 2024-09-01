package com.spvue.Image;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageRepository imageRepository;




    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;
    private final S3Presigner s3Presigner;
    String createPresignedUrl(String path) {
        var putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket) //올릴 버킷명
                .key(path) //경로
                .build();
        var preSignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60)) //URL 유효기간
                .putObjectRequest(putObjectRequest)
                .build();

        String presignedUrl = s3Presigner.presignPutObject(preSignRequest).url().toString();
        System.out.println("Generated Presigned URL: " + presignedUrl);

        return s3Presigner.presignPutObject(preSignRequest).url().toString();
    }

    public void saveImageInfo(String imageUrl, String imageName, String role){
        Image image = new Image();
        image.setImageUrl(imageUrl);
        image.setImageName(imageName);
        image.setRole(role);
        imageRepository.save(image);
    }




}
