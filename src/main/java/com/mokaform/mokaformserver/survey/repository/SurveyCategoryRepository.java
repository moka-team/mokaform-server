package com.mokaform.mokaformserver.survey.repository;

import com.mokaform.mokaformserver.survey.domain.Survey;
import com.mokaform.mokaformserver.survey.domain.SurveyCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyCategoryRepository extends JpaRepository<SurveyCategory, Long> {

    List<SurveyCategory> findSurveyCategoriesBySurvey(Survey survey);
}
