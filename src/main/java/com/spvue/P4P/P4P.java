package com.spvue.P4P;

import com.spvue.Boxer.Boxer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Setter
@Getter
@ToString
@Entity
public class P4P {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // primary key

    @ManyToOne
    @JoinColumn(name = "boxer_id", nullable = false)
    private Boxer boxer; // Boxer와의 관계 설정

    private Integer p4pScore;
    private Integer p4pRanking;
    private Integer previousRanking;


    private LocalDateTime rankingDate;
    public void updateRankingDate() {
        this.rankingDate = LocalDateTime.now();
    }
    
}
