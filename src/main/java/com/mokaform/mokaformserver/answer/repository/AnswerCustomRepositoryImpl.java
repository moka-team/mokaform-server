package com.mokaform.mokaformserver.answer.repository;

import com.mokaform.mokaformserver.answer.dto.mapping.AnswerInfoMapping;
import com.mokaform.mokaformserver.answer.dto.mapping.EssayAnswerStatsMapping;
import com.mokaform.mokaformserver.answer.dto.mapping.MultipleChoiceAnswerStatsMapping;
import com.mokaform.mokaformserver.answer.dto.mapping.OXAnswerStatsMapping;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

import java.util.List;

import static com.mokaform.mokaformserver.answer.domain.QAnswer.answer;
import static com.mokaform.mokaformserver.answer.domain.QEssayAnswer.essayAnswer;
import static com.mokaform.mokaformserver.answer.domain.QMultipleChoiceAnswer.multipleChoiceAnswer;
import static com.mokaform.mokaformserver.answer.domain.QOXAnswer.oXAnswer;
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

    @Override
    public List<EssayAnswerStatsMapping> findEssayAnswers(Long surveyId) {
        checkSurveyId(surveyId);

        return queryFactory
                .select(
                        Projections.fields(EssayAnswerStatsMapping.class,
                                question.title,
                                essayAnswer.answerContent))
                .from(survey)
                .leftJoin(question).on(survey.surveyId.eq(question.survey.surveyId))
                .leftJoin(answer).on(question.questionId.eq(answer.question.questionId))
                .leftJoin(essayAnswer).on(answer.answerId.eq(essayAnswer.answer.answerId))
                .where(
                        survey.surveyId.eq(surveyId),
                        essayAnswer.essayAnswerId.isNotNull())
                .fetch();
    }

    @Override
    public List<MultipleChoiceAnswerStatsMapping> findMultipleChoiceAnswers(Long surveyId) {
        checkSurveyId(surveyId);

        return queryFactory
                .select(
                        Projections.fields(MultipleChoiceAnswerStatsMapping.class,
                                question.title,
                                multipleChoiceAnswer.multipleChoiceQuestion.multiQuestionContent))
                .from(survey)
                .leftJoin(question).on(survey.surveyId.eq(question.survey.surveyId))
                .leftJoin(answer).on(question.questionId.eq(answer.question.questionId))
                .leftJoin(multipleChoiceAnswer).on(answer.answerId.eq(multipleChoiceAnswer.answer.answerId))
                .where(
                        survey.surveyId.eq(surveyId),
                        multipleChoiceAnswer.multipleChoiceAnswerId.isNotNull())
                .fetch();
    }

    @Override
    public List<OXAnswerStatsMapping> findOxAnswers(Long surveyId) {
        checkSurveyId(surveyId);

        return queryFactory
                .select(
                        Projections.fields(OXAnswerStatsMapping.class,
                                question.title,
                                oXAnswer.isYes))
                .from(survey)
                .leftJoin(question).on(survey.surveyId.eq(question.survey.surveyId))
                .leftJoin(answer).on(question.questionId.eq(answer.question.questionId))
                .leftJoin(oXAnswer).on(answer.answerId.eq(oXAnswer.answer.answerId))
                .where(
                        survey.surveyId.eq(surveyId),
                        oXAnswer.oxAnswerId.isNotNull())
                .fetch();
    }


    private void checkSurveyId(Long surveyId) {
        Assert.notNull(surveyId, SURVEY_ID_MUST_NOT_BE_NULL);
    }

    private void checkUserId(Long userId) {
        Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
    }

}
