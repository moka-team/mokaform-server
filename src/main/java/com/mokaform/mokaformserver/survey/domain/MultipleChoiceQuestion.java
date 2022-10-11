package com.mokaform.mokaformserver.survey.domain;

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
public class MultipleChoiceQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "multi_question_id", length = 320)
    private Long multi_question_id;

    @Column(name = "question_id", nullable = false, length = 320)
    private Long question_id;

    @Column(name = "multi_question_type", nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    private QuestionType multi_question_type;

    @Column(name = "multi_question_content", nullable = false, length = 255)
    private String multi_question_content;

    @Column(name = "multi_question_index", nullable = false)
    private Integer multi_question_index;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created_at;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updated_at;

    @Builder
    public MultipleChoiceQuestion(Long multi_question_id, Long question_id,
                                  QuestionType multi_question_type, String multi_question_content, Integer multi_question_index,
                                  LocalDateTime created_at, LocalDateTime updated_at) {
        this.multi_question_id = multi_question_id;
        this.question_id = question_id;
        this.multi_question_type = multi_question_type;
        this.multi_question_content = multi_question_content;
        this.multi_question_index = multi_question_index;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}