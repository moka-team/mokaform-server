package com.mokaform.mokaformserver.survey.domain;

import com.mokaform.mokaformserver.common.entitiy.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
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
    @Column(name = "survey_id")
    private Long survey_id;

    @Column(name = "surveyor_id", nullable = false, length = 320)
    private Long surveyor_id;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "start_date", nullable = true)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = true)
    private LocalDateTime endDate;

    @Column(name = "is_anonymous", nullable = false)
    private Boolean is_anonymous;

    @Column(name = "is_public", nullable = false)
    private Boolean is_public;

    @Column(name = "sharing_key", nullable = false, length = 36)
    private String sharing_key;

    @Column(name = "is_deleted", nullable = false)
    private Boolean is_deleted;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updated_at;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL)
    private List<QuestionTmp> questionTmpList = new ArrayList<>();

    @Builder
    public Survey(Long surveyor_id, String title,
                    Boolean is_anonymous, Boolean is_public, LocalDateTime start_date, LocalDateTime end_date
                  ) {
        this.surveyor_id = surveyor_id;
        this.title = title;
        this.startDate = start_date;
        this.endDate = end_date;
        this.is_anonymous = is_anonymous;
        this.is_public = is_public;
        this.sharing_key = UUID.randomUUID().toString();
        this.is_deleted = false;

    }
}
