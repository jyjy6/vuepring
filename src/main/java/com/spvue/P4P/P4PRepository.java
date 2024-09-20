package com.spvue.P4P;


import com.spvue.Boxer.Boxer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface P4PRepository extends JpaRepository<P4P, Long> {
    P4P findByBoxer(Boxer boxer);
    List<P4P> findByP4pRankingBetween(int startRank, int endRank);


//    @Modifying
//    @Query("UPDATE P4P p SET p.p4pRanking = p.p4pRanking + 1 WHERE p.p4pRanking BETWEEN :newRanking AND :oldRanking")
//    void decreaseRankingsBetween(@Param("newRanking") int newRanking, @Param("oldRanking") int oldRanking);
//
//    // 특정 랭킹 범위 내의 복서들의 순위를 1씩 올리는 쿼리
//    @Modifying
//    @Query("UPDATE P4P p SET p.p4pRanking = p.p4pRanking - 1 WHERE p.p4pRanking BETWEEN :oldRanking + 1 AND :newRanking")
//    void increaseRankingsBetween(@Param("oldRanking") int oldRanking, @Param("newRanking") int newRanking);
//



}
