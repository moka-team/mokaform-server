package com.mokaform.mokaformserver.answer.dto.response.stat;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
public class EssayStat extends QuestionInfo {

    private final List<String> answerContents;

    @Builder
    public EssayStat(Long questionIndex, String title,
                     List<String> answerContents) {
        super(questionIndex, title);
        this.answerContents = answerContents;
    }
}
