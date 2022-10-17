package com.mokaform.mokaformserver.survey.repository;

import com.mokaform.mokaformserver.survey.dto.mapping.SubmittedSurveyInfoMapping;
import com.mokaform.mokaformserver.survey.dto.mapping.SurveyInfoMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SurveyCustomRepository {

    Page<SurveyInfoMapping> findSurveyInfos(Pageable pageable, Long userId);

    Page<SubmittedSurveyInfoMapping> findSubmittedSurveyInfos(Pageable pageable, Long userId);

    Long countSurveyee(Long surveyId);
}
