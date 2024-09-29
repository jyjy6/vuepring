package com.spvue.Boxer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.awscore.util.SignerOverrideUtils;

import java.util.List;


@RequiredArgsConstructor
@Service
public class BoxerService {
    private final BoxerRepository boxerRepository;

    public void saveBoxer(Boxer boxer) {
        try {
            System.out.println("Saving boxer: " + boxer);
            boxerRepository.save(boxer);
        } catch (Exception e) {
            e.printStackTrace(); // 스택 트레이스 출력
        }
    }

    public List<Boxer> getAllBoxerByDivision(String weightClass) {
        List<Boxer> boxerList = boxerRepository.getAllBoxerByDivision(weightClass);
        return boxerList;
    }


    @Transactional
    public void updateBoxerRank(Boxer boxer){
        var boxerOldRanking = boxerRepository.findById(boxer.getId()).orElseThrow(() -> new RuntimeException("Value not found!"));
//        랭킹 변경 로직
        Integer oldRanking = boxerOldRanking.getRanking();
        System.out.println("올드랭킹 : " + oldRanking);
        Integer newRanking = boxer.getRanking();
        System.out.println("뉴랭킹 : " + newRanking);
        if(newRanking == oldRanking){
            return;
        }
        if (newRanking < oldRanking) {
            // 새로운 랭킹이 더 높은 경우 (예: 5위에서 2위로 올라감)
            List<Boxer> affectedBoxers =
                    boxerRepository.findByDivisionAndRankingBetween( boxer.getDivision(), newRanking, oldRanking - 1);
            System.out.println("해당복서 :" + affectedBoxers);
            for (Boxer box : affectedBoxers) {
                box.setRanking(box.getRanking() + 1); // 랭킹을 한 단계씩 내림
                boxerRepository.save(box);
            }
        } else if (newRanking > oldRanking) {
            // 새로운 랭킹이 더 낮은 경우 (예: 2위에서 5위로 내려감)

            List<Boxer> affectedBoxers = boxerRepository.findByDivisionAndRankingBetween(boxer.getDivision(),oldRanking + 1, newRanking);
            System.out.println("해당복서 :" + affectedBoxers);
            for (Boxer box : affectedBoxers) {
                box.setRanking(box.getRanking() - 1); // 랭킹을 한 단계씩 올림
                boxerRepository.save(box);
            }
        }


    }

}
