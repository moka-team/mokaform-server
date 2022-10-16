package com.mokaform.mokaformserver.survey.repository;

import com.mokaform.mokaformserver.survey.domain.SurveyCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyCategoryRepository extends JpaRepository<SurveyCategory, Long> {
}
