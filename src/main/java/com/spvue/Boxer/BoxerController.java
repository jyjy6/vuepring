package com.spvue.Boxer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/boxers")
public class BoxerController {

    private final BoxerService boxerService;

    @PostMapping("/add")
    public ResponseEntity<String> addBoxer(@RequestBody Boxer boxer) {
        try {
            boxerService.saveBoxer(boxer);
            return ResponseEntity.ok("성공적으로 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 발생: " + e.getMessage());
        }
    }

//   겟매핑으로 복서의 체급을 받아서 해당 복서의 체급에 맞는 행들 다 가져와서 리스트에 넣어주세요~ 대신 정렬은 순위에 맞게 오름차순으로 해주세요.
@GetMapping
public List<Boxer> getAllBoxerByWeightClass(String weightClass) {
    var result = boxerService.getAllBoxerByDivision(weightClass);
    System.out.println(result);
    return result;
}


}
