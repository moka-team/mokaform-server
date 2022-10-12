package com.mokaform.mokaformserver.survey.domain;

import com.mokaform.mokaformserver.common.entitiy.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column(name = "sharing_key", nullable = false, length = 20)
    private String sharingKey;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;


    @Builder
    public Survey(Long surveyorId, String title,
                    Boolean isAnonymous, Boolean isPublic, String sharingKey) {
        this.surveyorId = surveyorId;
        this.title = title;
        //TODO: startDate, endDate 제대로 받아오기
        this.startDate = LocalDateTime.now();
        this.endDate = LocalDateTime.now();
        this.isAnonymous = isAnonymous;
        this.isPublic = isPublic;
        this.sharingKey = sharingKey;
        this.isDeleted = false;
    }
}
