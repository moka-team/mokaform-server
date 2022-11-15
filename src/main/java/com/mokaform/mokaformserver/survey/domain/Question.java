package com.mokaform.mokaformserver.survey.domain;

import com.mokaform.mokaformserver.common.entitiy.BaseEntity;
import com.mokaform.mokaformserver.survey.domain.enums.QuestionType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "question")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", referencedColumnName = "survey_id", nullable = false)
    private Survey survey;

    @Column(name = "question_title", length = 255, nullable = false)
    private String title;

    @Column(name = "question_index", nullable = false)
    private Long index;

    @Column(name = "question_type", length = 20, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private QuestionType type;

    @Column(name = "is_multi_answer", nullable = false)
    private Boolean isMultiAnswer;

    @OneToMany(mappedBy = "question")
    private List<MultipleChoiceQuestion> multipleChoiceQuestions = new ArrayList<>();

    @Builder
    public Question(Survey survey, String title,
                    Long index, QuestionType type,
                    Boolean isMultiAnswer) {
        this.setSurvey(survey);
        this.title = title;
        this.index = index;
        this.type = type;
        this.isMultiAnswer = isMultiAnswer;
    }

    public void setSurvey(Survey survey) {
        if (Objects.nonNull(this.survey)) {
            this.survey.getQuestions().remove(this);
        }

        this.survey = survey;
        survey.getQuestions().add(this);
    }

    public void updateQuestion(Long index, String title,
                               QuestionType type, Boolean isMultiAnswer) {
        this.index = index;
        this.title = title;
        this.type = type;
        this.isMultiAnswer = isMultiAnswer;
    }
}
