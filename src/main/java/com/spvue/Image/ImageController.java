package com.spvue.Image;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;


    @ResponseBody
    @GetMapping("/presigned-url")
    public String getImgUrl(@RequestParam String filename,
                            @RequestParam String role){
        String decodedFilename = URLDecoder.decode(filename, StandardCharsets.UTF_8);
        var decodedRandomFilename = UUID.randomUUID().toString() + "_" + decodedFilename;
        var result = imageService.createPresignedUrl("test/" + decodedRandomFilename);
        String baseUrl = result.split("\\?")[0]; //생성된 Presigned-URL에서 잡부분 제거하고 실제이미지URL만 남김
        imageService.saveImageInfo(baseUrl, decodedFilename, role);
//        지금은 업로드한것만으로 이미지를 저장하고있는데 나중에는 업로드완료하고글작성하면 이미지정보 DB에 저장할 수 있도록 생각해보자.

        return result;
    }

}
