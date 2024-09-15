package com.spvue.Image;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByImgUsedFalse();
    Optional<Image> findByImageUrl(String URL);
}
