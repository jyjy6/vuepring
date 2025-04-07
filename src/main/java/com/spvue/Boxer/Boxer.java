package com.spvue.Boxer;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@ToString
@Entity
public class Boxer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // primary key

    private String division;
    private String name;
    private Integer rating;
    private Integer bouts;
    private Integer rounds;
    private String ko;
    private String career;
    private String debut;
    private String title;
    @JsonProperty("birthname")
    private String birthName;
    private String sex;
    private Integer age;
    private String country;
    private String stance;
    private String reach;
    private String height;
    @JsonProperty("birthplace")
    private String birthPlace;
    private String author;
    private Integer ranking;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private String boxerImg;


    //생성자는 걍 NoArgs AlLArgs하면되는데 있어보이려고 이 엔티티에만 직접적어놓음

    // 기본 생성자
    public Boxer() {
    }

    // 모든 필드를 포함한 생성자
    public Boxer(Long id, String division, String name, Integer rating, Integer bouts, Integer rounds, String ko,
                 String career, String debut, String title, String birthName, String sex, Integer age,
                 String country, String stance, String reach, String height, String birthPlace, String author, Integer ranking, LocalDateTime createdAt, String boxerImg) {
        this.id = id;
        this.division = division;
        this.name = name;
        this.rating = rating;
        this.bouts = bouts;
        this.rounds = rounds;
        this.ko = ko;
        this.career = career;
        this.debut = debut;
        this.title = title;
        this.birthName = birthName;
        this.sex = sex;
        this.age = age;
        this.country = country;
        this.stance = stance;
        this.reach = reach;
        this.height = height;
        this.birthPlace = birthPlace;
        this.ranking = ranking;
        this.createdAt = createdAt;
        this.boxerImg = boxerImg;
    }

    // Getter와 Setter 메서드들
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getBouts() {
        return bouts;
    }

    public void setBouts(Integer bouts) {
        this.bouts = bouts;
    }

    public Integer getRounds() {
        return rounds;
    }

    public void setRounds(Integer rounds) {
        this.rounds = rounds;
    }

    public String getKo() {
        return ko;
    }

    public void setKo(String ko) {
        this.ko = ko;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getDebut() {
        return debut;
    }

    public void setDebut(String debut) {
        this.debut = debut;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBirthName() {
        return birthName;
    }

    public void setBirthName(String birthName) {
        this.birthName = birthName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStance() {
        return stance;
    }

    public void setStance(String stance) {
        this.stance = stance;
    }

    public String getReach() {
        return reach;
    }

    public void setReach(String reach) {
        this.reach = reach;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getBoxerImg() {
        return boxerImg;
    }

    public void setBoxerImg(String boxerImg) {
        this.boxerImg = boxerImg;
    }
}
