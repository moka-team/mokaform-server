package com.mokaform.mokaformserver.survey.domain;

import com.mokaform.mokaformserver.common.entitiy.BaseEntity;
import com.mokaform.mokaformserver.survey.domain.enums.MultiQuestionType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "multiple_choice_question")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MultipleChoiceQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "multi_question_id", length = 320)
    private Long multiQuestionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", referencedColumnName = "question_id", nullable = false)
    private Question questionTmp;

    @Column(name = "multi_question_type", nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    private MultiQuestionType multiQuestionType;

    @Column(name = "multi_question_content", nullable = false, length = 255)
    private String multiQuestionContent;

    @Column(name = "multi_question_index", nullable = false)
    private Integer multiQuestionIndex;

    @Builder
    public MultipleChoiceQuestion(Question questionTmp,
                                  MultiQuestionType multiQuestionType, String multiQuestionContent, Integer multiQuestionIndex) {
        this.questionTmp = questionTmp;
        this.multiQuestionType = multiQuestionType;
        this.multiQuestionContent = multiQuestionContent;
        this.multiQuestionIndex = multiQuestionIndex;
    }
}