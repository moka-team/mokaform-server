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


    @Column(name = "question_id", nullable = false, length = 320)
    private Long question_id;

    @Column(name = "multi_question_type", nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    private MultiQuestionType multi_question_type;

    @Column(name = "multi_question_content", nullable = false, length = 255)
    private String multi_question_content;

    @Column(name = "multi_question_index", nullable = false)
    private Integer multi_question_index;

    @Builder
    public MultipleChoiceQuestion(Long question_id,
                                  MultiQuestionType multi_question_type, String multi_question_content, Integer multi_question_index) {
        this.question_id = question_id;
        this.multi_question_type = multi_question_type;
        this.multi_question_content = multi_question_content;
        this.multi_question_index = multi_question_index;
    }
}