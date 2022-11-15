package com.mokaform.mokaformserver.survey.domain;

import com.mokaform.mokaformserver.common.entitiy.BaseEntity;
import com.mokaform.mokaformserver.survey.domain.enums.MultiQuestionType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "multiple_choice_question")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MultipleChoiceQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "multi_question_id")
    private Long multiQuestionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", referencedColumnName = "question_id", nullable = false)
    private Question question;

    @Column(name = "multi_question_type", nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    private MultiQuestionType multiQuestionType;

    @Column(name = "multi_question_content", nullable = false, length = 255)
    private String multiQuestionContent;

    @Column(name = "multi_question_index", nullable = false)
    private Long multiQuestionIndex;

    @Builder
    public MultipleChoiceQuestion(Question question, MultiQuestionType multiQuestionType,
                                  String multiQuestionContent, Long multiQuestionIndex) {
        this.setQuestion(question);
        this.multiQuestionType = multiQuestionType;
        this.multiQuestionContent = multiQuestionContent;
        this.multiQuestionIndex = multiQuestionIndex;
    }

    public void setQuestion(Question question) {
        if (Objects.nonNull(this.question)) {
            this.question.getMultipleChoiceQuestions().remove(this);
        }

        this.question = question;
        question.getMultipleChoiceQuestions().add(this);
    }

    public void unsetQuestion() {
        if (Objects.nonNull(this.question)) {
            this.question.getMultipleChoiceQuestions().remove(this);
        }
    }

    public void updateMultipleChoiceQuestion(MultiQuestionType type, String content,
                                             Long index) {
        this.multiQuestionType = type;
        this.multiQuestionContent = content;
        this.multiQuestionIndex = index;
    }
}