package com.mokaform.mokaformserver.answer.dto.response.stat;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
public class QuestionInfo {

    private final Long questionIndex;

    private final String title;

    public QuestionInfo(Long questionIndex, String title) {
        this.questionIndex = questionIndex;
        this.title = title;
    }
}
