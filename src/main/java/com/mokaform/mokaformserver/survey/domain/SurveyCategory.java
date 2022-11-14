package com.mokaform.mokaformserver.survey.domain;

import com.mokaform.mokaformserver.common.entitiy.BaseEntity;
import com.mokaform.mokaformserver.survey.domain.enums.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "survey_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SurveyCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_category_id")
    private Long id;

    @Column(name = "category_name", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", referencedColumnName = "survey_id", nullable = false)
    private Survey survey;

    public SurveyCategory(Category category, Survey survey) {
        this.category = category;
        this.setSurvey(survey);
    }

    public void setSurvey(Survey survey) {
        if (Objects.nonNull(this.survey)) {
            this.survey.getCategories().remove(this);
        }

        this.survey = survey;
        survey.getCategories().add(this);
    }

    public void unsetSurvey() {
        if (Objects.nonNull(this.survey)) {
            this.survey.getCategories().remove(this);
        }
    }
}
