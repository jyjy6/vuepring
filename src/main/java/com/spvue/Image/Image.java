package com.spvue.Image;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Image {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    private String imageUrl;
    private String imageName;




}
