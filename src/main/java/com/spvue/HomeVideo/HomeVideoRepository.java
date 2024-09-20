package com.spvue.HomeVideo;


import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeVideoRepository extends JpaRepository<HomeVideo, Long> {
    HomeVideo findTopByOrderByIdDesc();
}
