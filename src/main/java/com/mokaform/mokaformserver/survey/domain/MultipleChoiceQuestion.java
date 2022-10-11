package com.mokaform.mokaformserver.survey.domain;

import com.mokaform.mokaformserver.common.entitiy.BaseEntity;
import com.mokaform.mokaformserver.survey.domain.enums.MultiQuestionType;
import com.mokaform.mokaformserver.survey.domain.enums.QuestionType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "multiple_choice_question")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MultipleChoiceQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "multi_question_id", length = 320)
    private Long multi_question_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", referencedColumnName = "question_id", nullable = false)
    private QuestionTmp questionTmp;

    @Column(name = "multi_question_type", nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    private MultiQuestionType multi_question_type;

    @Column(name = "multi_question_content", nullable = false, length = 255)
    private String multi_question_content;

    @Column(name = "multi_question_index", nullable = false)
    private Integer multi_question_index;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updated_at;

    @Builder
    public MultipleChoiceQuestion(QuestionTmp questionTmp,
                                  MultiQuestionType multi_question_type, String multi_question_content, Integer multi_question_index) {
        this.questionTmp = questionTmp;
        this.multi_question_type = multi_question_type;
        this.multi_question_content = multi_question_content;
        this.multi_question_index = multi_question_index;
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }
}