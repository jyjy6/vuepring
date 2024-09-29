package com.spvue.Boxer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoxerRepository extends JpaRepository<Boxer, Long> {

    List<Boxer> getAllBoxerByDivision(String weightClass);
    List<Boxer> findByDivisionAndRankingBetween(String division, Integer startRanking, Integer endRanking);


    @Query("SELECT b.ranking FROM Boxer b WHERE b.id = :id")
    Integer findRankingById(Long id);

}
