package com.spvue.P4P.P4PRankingChangeLog;

import com.spvue.Boxer.Boxer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Setter
@Getter
@Entity
public class P4PRankingChangeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime changeDate; // 순위 변경 날짜

    @ElementCollection
    @CollectionTable(name = "p4p_ranking", joinColumns = @JoinColumn(name = "ranking_log_id"))
    @MapKeyJoinColumn(name = "boxer_id")
    @Column(name = "ranking")
    private Map<Boxer, Integer> rankings; // 복서와 순위를 매핑한 Map

    // 기본 생성자 및 필요한 메서드
    public P4PRankingChangeLog() {
        this.changeDate = LocalDateTime.now();
    }

    public P4PRankingChangeLog(Map<Boxer, Integer> rankings) {
        this.rankings = rankings;
        this.changeDate = LocalDateTime.now();
    }

}
