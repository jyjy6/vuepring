package com.spvue.Boxer;

import com.spvue.Image.Image;
import com.spvue.Image.ImageRepository;
import com.spvue.Image.ImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/boxers")
public class BoxerController {

    private final BoxerService boxerService;
    private final ImageService imageService;
    private final BoxerRepository boxerRepository;



    @PostMapping("/add")
    public ResponseEntity<String> addBoxer(@RequestBody Boxer boxer) {
        try {
            boxerService.saveBoxer(boxer);
            String[] imgURL = new String[]{boxer.getBoxerImg()};
            imageService.imageFinalSave(imgURL);

            return ResponseEntity.ok("성공적으로 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 발생: " + e.getMessage());
        }
    }

//   겟매핑으로 복서의 체급을 받아서 해당 복서의 체급에 맞는 행들 다 가져와서 리스트에 넣어주세요~ 대신 정렬은 순위에 맞게 오름차순으로 해주세요.
    @GetMapping
    public List<Boxer> getAllBoxerByWeightClass(String weightClass) {
        var result = boxerService.getAllBoxerByDivision(weightClass);

        // ranking 기준으로 오름차순 정렬
        result.sort(Comparator.comparingInt(Boxer::getRanking));

        return result;
    }



    @PutMapping("/modify")
    public ResponseEntity<String> modifyBoxer(@RequestBody Boxer boxer) {
        try {
            boxerService.updateBoxerRank(boxer);
            boxerService.saveBoxer(boxer);
            String[] imgURL = new String[] {boxer.getBoxerImg()};
            imageService.imageFinalSave(imgURL);

            return ResponseEntity.ok("성공적으로 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 발생: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Boxer getBoxerData(@PathVariable Long id){
        Boxer result = boxerRepository.findById(id).orElseThrow(() -> new RuntimeException("Value not found!"));
        return result;
    }

}
