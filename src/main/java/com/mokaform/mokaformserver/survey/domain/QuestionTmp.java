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
    private Long questionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", referencedColumnName = "survey_id")
    private Survey survey;

    @Column(name = "question_title", length = 255)
    private String title;

    @Column(name = "question_index")
    private Long index;

    @Column(name = "question_type", length = 20)
    private QuestionType type;

    @Column(name = "is_multi_answer")
    private Boolean isMultiAnswer;


    @Builder
    public QuestionTmp(Survey survey, String title, Long index, QuestionType type, Boolean isMultiAnswer){
        this.survey = survey;
        this.title= title;
        this.index = index;
        this.type = type;
        this.isMultiAnswer = isMultiAnswer;
    }
}
