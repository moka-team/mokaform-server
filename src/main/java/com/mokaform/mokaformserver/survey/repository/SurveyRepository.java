package com.mokaform.mokaformserver.survey.repository;

import com.mokaform.mokaformserver.survey.domain.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
