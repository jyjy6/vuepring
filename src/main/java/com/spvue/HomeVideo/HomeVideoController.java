package com.spvue.HomeVideo;


import com.spvue.Boxer.Boxer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/homevideo")
public class HomeVideoController {
    private final HomeVideoService homeVideoService;
    private final HomeVideoRepository homeVideoRepository;

    @PostMapping("/write")
    public ResponseEntity<String> writeHomeVideo(@RequestBody HomeVideo post) {
        try {
            homeVideoService.savePost(post);

            return ResponseEntity.ok("성공적으로 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 발생: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<HomeVideo> returnVideo() {
        HomeVideo result = homeVideoRepository.findTopByOrderByIdDesc();
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build(); // 404 반환
        }
    }


}
