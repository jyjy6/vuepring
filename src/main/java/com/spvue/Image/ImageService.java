package com.spvue.Image;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

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

        return presignedUrl;
    }

    public void saveImageInfo(String imageUrl, String imageName, String role){
        Image image = new Image();
        image.setImageUrl(imageUrl);
        image.setImageName(imageName);
        image.setRole(role);
        imageRepository.save(image);
    }

    public void imageFinalSave(String[] imgURL){
        for (String img : imgURL){
            Optional<Image> imageOptional = imageRepository.findByImageUrl(img);
            if (imageOptional.isPresent()) {
                Image image = imageOptional.get();
                image.setImgUsed(true);  // imgUsed 값을 true로 변경
                imageRepository.save(image);  // 변경된 값 저장
            }
        }
    }

    public String extractS3Key(String imageUrl) {
        // S3 URL에서 버킷 이름과 도메인 부분을 제거하고 나머지 경로만 추출
        return imageUrl.substring(imageUrl.indexOf("test"));  // 'test'(폴더이름) 경로 이후부터 추출. 추후 실제 서비스에서는 이미지 저장경로에 맞게 변경
    }




}
