package com.spvue.P4P;

import com.spvue.Boxer.Boxer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class P4PService {
    private final P4PRepository p4pRepository;

    @Transactional
    public void updateBoxerRanking(Boxer boxer, Integer p4pScore, Integer newRanking) {
        // 변경할 복서의 P4P 엔티티 조회

        P4P targetP4P = p4pRepository.findByBoxer(boxer);
        if(targetP4P==null){
            targetP4P = new P4P();
            targetP4P.setBoxer(boxer);
            targetP4P.setP4pScore(p4pScore);
            targetP4P.setP4pRanking(9999);
            targetP4P.updateRankingDate(); // 랭킹 변경 날짜 업데이트
            p4pRepository.save(targetP4P); // 변경 사항 저장
        }

        // 기존 랭킹과 새로운 랭킹 비교
        Integer oldRanking;
        if(targetP4P.getP4pRanking() == null) {
            oldRanking = 9999;
        } else {
            oldRanking = targetP4P.getP4pRanking();
        }

        if (newRanking < oldRanking) {
            // 만약 새로운 랭킹이 기존 랭킹보다 높다면 (예: 10위에서 1위로 올라감)
            // 기존의 새로운 랭킹보다 높은 복서들의 순위를 1씩 내림
            p4pRepository.decreaseRankingsBetween(newRanking, oldRanking);
        } else if (newRanking > oldRanking) {
            // 만약 새로운 랭킹이 기존 랭킹보다 낮다면 (예: 1위에서 10위로 내려감)
            // 기존의 새로운 랭킹보다 낮은 복서들의 순위를 1씩 올림
            p4pRepository.increaseRankingsBetween(oldRanking, newRanking);
        }

        // 변경된 랭킹을 적용
        targetP4P.setP4pRanking(newRanking);
        targetP4P.updateRankingDate(); // 랭킹 변경 날짜 업데이트
        p4pRepository.save(targetP4P); // 변경 사항 저장

    }







}
