package com.mokaform.mokaformserver.answer.dto.response.stat;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
public class MultipleChoiceStat extends QuestionInfo {

    private final List<MultipleChoiceResult> results;

    @Builder
    public MultipleChoiceStat(Long questionIndex, String title,
                              List<MultipleChoiceResult> results) {
        super(questionIndex, title);
        this.results = results;
    }
}
