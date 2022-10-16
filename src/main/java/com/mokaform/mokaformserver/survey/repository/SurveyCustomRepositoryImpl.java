package com.mokaform.mokaformserver.survey.repository;

import com.mokaform.mokaformserver.survey.dto.mapping.SurveyInfoMapping;
import com.mokaform.mokaformserver.survey.repository.enums.SurveySortType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.mokaform.mokaformserver.answer.domain.QAnswer.answer;
import static com.mokaform.mokaformserver.common.util.QuerydslCustomUtils.nullSafeBooleanBuilder;
import static com.mokaform.mokaformserver.survey.domain.QQuestion.question;
import static com.mokaform.mokaformserver.survey.domain.QSurvey.survey;

@RequiredArgsConstructor
public class SurveyCustomRepositoryImpl implements SurveyCustomRepository {

    private static final String PAGEABLE_MUST_NOT_BE_NULL = "The given pageable must not be null!";

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<SurveyInfoMapping> findSurveyInfos(Pageable pageable) {
        checkPageable(pageable);

        List<SurveyInfoMapping> content = queryFactory
                .select(
                        Projections.fields(SurveyInfoMapping.class,
                                survey.surveyId,
                                survey.title,
                                survey.summary,
                                survey.startDate,
                                survey.endDate,
                                survey.isAnonymous,
                                survey.isPublic,
                                survey.sharingKey,
                                answer.user.id.countDistinct().as("surveyeeCount")))
                .from(survey)
                .leftJoin(question).on(survey.surveyId.eq(question.survey.surveyId))
                .leftJoin(answer).on(question.questionId.eq(answer.question.questionId))
                .where(
                        survey.isPublic.isTrue(),
                        survey.isDeleted.isFalse(),
                        filterOngoingSurvey())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(survey.surveyId)
                .orderBy(getAllOrderSpecifiers(pageable).toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(survey.countDistinct())
                .from(survey)
                .where(survey.isPublic.isTrue(),
                        survey.isDeleted.isFalse(),
                        filterOngoingSurvey());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanBuilder filterOngoingSurvey() {
        LocalDate now = LocalDate.now();
        return nullSafeBooleanBuilder(() ->
                survey.startDate.loe(now).and(survey.endDate.goe(now)));
    }

    private void checkPageable(Pageable pageable) {
        Assert.notNull(pageable, PAGEABLE_MUST_NOT_BE_NULL);
    }

    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
        if (pageable.getSort().isEmpty()) {
            return Collections.emptyList();
        }

        return pageable.getSort().stream()
                .map(order -> SurveySortType.getSortType(order.getProperty())
                        .getOrderSpecifier(order.getDirection().isAscending() ? Order.ASC : Order.DESC))
                .collect(Collectors.toList());
    }
}
