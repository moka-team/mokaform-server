package com.mokaform.mokaformserver.survey.repository;

import com.mokaform.mokaformserver.survey.dto.mapping.SurveyInfoMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SurveyCustomRepository {

    Page<SurveyInfoMapping> findSurveyInfos(Pageable pageable);
}
