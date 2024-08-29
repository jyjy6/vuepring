package com.spvue.Image;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;


    @ResponseBody
    @GetMapping("/presigned-url")
    public String getImgUrl(@RequestParam String filename) {
        String decodedFilename = URLDecoder.decode(filename, StandardCharsets.UTF_8);
        System.out.println(decodedFilename);
        var result = imageService.createPresignedUrl("test/" + decodedFilename);
        return result;
    }

}
