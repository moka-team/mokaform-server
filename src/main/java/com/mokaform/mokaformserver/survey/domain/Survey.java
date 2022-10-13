package com.mokaform.mokaformserver.survey.domain;

import com.mokaform.mokaformserver.common.entitiy.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "survey")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Survey extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id", length = 320)
    private Long surveyId;

    // TODO: Users 테이블이랑 연관 관계 매핑
    @Column(name = "surveyor_id", nullable = false, length = 320)
    private Long surveyorId;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "is_anonymous", nullable = false)
    private Boolean isAnonymous;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Column(name = "sharing_key", nullable = false, length = 36)
    private String sharing_key;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;


    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL)
    private List<Question> questionTmpList = new ArrayList<>();

    @Builder
    public Survey(Long surveyor_id, String title,
                  Boolean is_anonymous, Boolean is_public, LocalDateTime start_date, LocalDateTime end_date
    ) {
        this.surveyorId = surveyor_id;
        this.title = title;
        this.startDate = start_date;
        this.endDate = end_date;
        this.isAnonymous = is_anonymous;
        this.isPublic = is_public;
        this.sharing_key = UUID.randomUUID().toString();
        this.isDeleted = false;

    }
}
