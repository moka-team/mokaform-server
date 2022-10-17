package com.mokaform.mokaformserver.answer.repository;

import com.mokaform.mokaformserver.answer.dto.mapping.AnswerInfoMapping;
import com.mokaform.mokaformserver.answer.dto.mapping.EssayAnswerStatsMapping;
import com.mokaform.mokaformserver.answer.dto.mapping.MultipleChoiceAnswerStatsMapping;
import com.mokaform.mokaformserver.answer.dto.mapping.OXAnswerStatsMapping;

import java.util.List;

public interface AnswerCustomRepository {

    List<AnswerInfoMapping> findAnswerInfos(Long surveyId, Long userId);

    List<EssayAnswerStatsMapping> findEssayAnswers(Long surveyId);

    List<MultipleChoiceAnswerStatsMapping> findMultipleChoiceAnswers(Long surveyId);

    List<OXAnswerStatsMapping> findOxAnswers(Long surveyId);
}
