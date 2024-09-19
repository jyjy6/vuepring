package com.spvue.P4P;

import com.spvue.Boxer.Boxer;
import com.spvue.P4P.P4PRankingChangeLog.P4PRankingChangeLog;
import com.spvue.P4P.P4PRankingChangeLog.P4PRankingChangeLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class P4PService {
    private final P4PRepository p4pRepository;
    private final P4PRankingChangeLogRepository p4PRankingChangeLogRepository;

    @Transactional
    public void updateBoxerRanking(Boxer boxer, Integer p4pScore, Integer newRanking) {
        // 변경할 복서의 P4P 엔티티 조회
        P4P targetP4P = p4pRepository.findByBoxer(boxer);
        if (targetP4P == null) {
            targetP4P = new P4P();
            targetP4P.setBoxer(boxer);
            targetP4P.setP4pScore(p4pScore);
            targetP4P.setP4pRanking(9999);
            targetP4P.updateRankingDate(); // 랭킹 변경 날짜 업데이트
            p4pRepository.save(targetP4P); // 변경 사항 저장
        }

        // 기존 랭킹과 새로운 랭킹 비교
        Integer oldRanking = targetP4P.getP4pRanking() != null ? targetP4P.getP4pRanking() : 9999;

        // 해당 복서의 새로운 랭킹을 임시로 설정
        targetP4P.setP4pRanking(newRanking);
        targetP4P.updateRankingDate(); // 랭킹 변경 날짜 업데이트
        p4pRepository.save(targetP4P); // 임시 저장 (복서의 새로운 랭킹만 적용)

        // 모든 복서를 가져옴
        List<P4P> allP4PBoxers = p4pRepository.findAll(Sort.by("p4pRanking"));

        // 모든 복서들의 순위를 다시 계산
        for (P4P p4p : allP4PBoxers) {
            if (!p4p.getBoxer().equals(boxer)) {
                int currentRanking = p4p.getP4pRanking();
                p4p.setPreviousRanking(currentRanking);

                // 새로운 랭킹 범위 안에 있는 복서들의 순위 조정
                if (currentRanking >= newRanking && currentRanking < oldRanking) {
                    // 복서 A가 1위에서 8위로 떨어질 때, 기존 8위였던 복서들은 7위가 되어야 함.
                    p4p.setP4pRanking(currentRanking + 1); // 복서들의 순위를 한 단계 올림
                } else if (currentRanking > oldRanking && currentRanking <= newRanking) {
                    // 복서 A가 8위에서 1위로 올라갈 때, 기존 7위 이하의 복서들은 8위가 되어야 함.
                    p4p.setP4pRanking(currentRanking - 1); // 복서들의 순위를 한 단계 내림
                }
                p4p.updateRankingDate(); // 복서들의 순위 변경 날짜 업데이트
                p4pRepository.save(p4p); // 순위 업데이트

            }
        }

        // 모든 복서의 변경된 랭킹 맵 생성
        Map<Boxer, Integer> rankingMap = allP4PBoxers.stream()
                .collect(Collectors.toMap(P4P::getBoxer, P4P::getP4pRanking));

        // 순위 변경 이력 저장
        P4PRankingChangeLog log = new P4PRankingChangeLog(rankingMap);
        p4PRankingChangeLogRepository.save(log);
    }








}
