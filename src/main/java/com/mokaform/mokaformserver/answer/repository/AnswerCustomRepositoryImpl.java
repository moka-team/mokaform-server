package com.mokaform.mokaformserver.answer.repository;

import com.mokaform.mokaformserver.answer.dto.mapping.AnswerInfoMapping;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

import java.util.List;

import static com.mokaform.mokaformserver.answer.domain.QAnswer.answer;
import static com.mokaform.mokaformserver.survey.domain.QQuestion.question;
import static com.mokaform.mokaformserver.survey.domain.QSurvey.survey;

@RequiredArgsConstructor
public class AnswerCustomRepositoryImpl implements AnswerCustomRepository {

    private static final String SURVEY_ID_MUST_NOT_BE_NULL = "The given survey id must not be null!";
    private static final String USER_ID_MUST_NOT_BE_NULL = "The given user id must not be null!";

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AnswerInfoMapping> findAnswerInfos(Long surveyId, Long userId) {
        checkSurveyId(surveyId);
        checkUserId(userId);
        
        return queryFactory
                .select(
                        Projections.fields(AnswerInfoMapping.class,
                                answer.answerId,
                                answer.question.questionId))
                .from(survey)
                .leftJoin(question).on(survey.surveyId.eq(question.survey.surveyId))
                .leftJoin(answer).on(question.questionId.eq(answer.question.questionId))
                .where(
                        survey.surveyId.eq(surveyId),
                        answer.user.id.eq(userId))
                .fetch();
    }

    private void checkSurveyId(Long surveyId) {
        Assert.notNull(surveyId, SURVEY_ID_MUST_NOT_BE_NULL);
    }

    private void checkUserId(Long userId) {
        Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
    }

}
