package com.spvue.Boxer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoxerRepository extends JpaRepository<Boxer, Long> {

    List<Boxer> getAllBoxerByDivision(String weightClass);
    List<Boxer> findByDivisionAndRankingBetween(String division, Integer startRanking, Integer endRanking);


    Integer findRankingById(Long id);
}
