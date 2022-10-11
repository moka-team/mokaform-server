package com.mokaform.mokaformserver.survey.domain;

import com.mokaform.mokaformserver.common.entitiy.BaseEntity;
import com.mokaform.mokaformserver.survey.domain.enums.QuestionType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "question_tmp")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class QuestionTmp extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "question_id")
    private Long id;

    @Column(name = "survey_id")
    private Long survey_id;

    @Column(name = "question_title", length = 255)
    private String title;

    @Column(name = "question_index")
    private Long index;

    @Column(name = "question_type", length = 20)
    private QuestionType type;

    @Column(name = "is_multi_answer")
    private Boolean is_multi_answer;

    @Column(name = "created_at",insertable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(name = "updated_at",insertable = false, updatable = false)
    private LocalDateTime updated_at;


    @Builder
    public QuestionTmp(Long survey_id, String title, Long index, QuestionType type, Boolean is_multi_answer, LocalDateTime created_at, LocalDateTime updated_at){
        this.survey_id = survey_id;
        this.title= title;
        this.index = index;
        this.type = type;
        this.is_multi_answer = is_multi_answer;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
