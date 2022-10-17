package com.mokaform.mokaformserver.answer.repository;

import com.mokaform.mokaformserver.answer.dto.mapping.AnswerInfoMapping;

import java.util.List;

public interface AnswerCustomRepository {

    List<AnswerInfoMapping> findAnswerInfos(Long surveyId, Long userId);
}
