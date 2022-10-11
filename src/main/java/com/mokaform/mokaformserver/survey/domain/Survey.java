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
    private Long survey_id;

    @Column(name = "surveyor_id", nullable = false, length = 320)
    private Long surveyor_id;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime start_date;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime end_date;

    @Column(name = "is_anonymous", nullable = false)
    private Boolean is_anonymous;

    @Column(name = "is_public", nullable = false)
    private Boolean is_public;

    @Column(name = "sharing_key", nullable = false, length = 20)
    private String sharing_key;

    @Column(name = "is_deleted", nullable = false)
    private Boolean is_deleted;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created_at;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updated_at;

    @Builder
    public Survey(Long survey_id, Long surveyor_id, String title,
                    LocalDateTime start_date, LocalDateTime end_date, Boolean is_anonymous,
                    Boolean is_public, String sharing_key, Boolean is_deleted,
                    LocalDateTime created_at, LocalDateTime updated_at) {
        this.survey_id = survey_id;
        this.surveyor_id = surveyor_id;
        this.title = title;
        this.start_date = start_date;
        this.end_date = end_date;
        this.is_anonymous = is_anonymous;
        this.is_public = is_public;
        this.sharing_key = sharing_key;
        this.is_deleted = is_deleted;
        this.created_at = created_at;
        this.updated_at = updated_at;

    }
}