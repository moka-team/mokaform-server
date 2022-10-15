package com.mokaform.mokaformserver.survey.repository.enums;

import com.mokaform.mokaformserver.common.exception.ApiException;
import com.mokaform.mokaformserver.common.exception.errorcode.SurveyErrorCode;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;

import java.util.Arrays;

import static com.mokaform.mokaformserver.answer.domain.QAnswer.answer;
import static com.mokaform.mokaformserver.survey.domain.QSurvey.survey;

public enum SurveySortType {

    CREATED_AT("createdAt", survey.createdAt),
    SURVEYEE_COUNT("surveyeeCount", answer.user.id.countDistinct());

    private final String property;
    private final Expression target;

    SurveySortType(String property, Expression target) {
        this.property = property;
        this.target = target;
    }

    public OrderSpecifier<?> getOrderSpecifier(Order direction) {
        return new OrderSpecifier(direction, this.target);
    }

    public static SurveySortType getSortType(String property) {
        return Arrays.stream(SurveySortType.values())
                .filter(sortType -> sortType.property.equals(property))
                .findAny()
                .orElseThrow(() ->
                        new ApiException(SurveyErrorCode.INVALID_SORT_TYPE));
    }
}
