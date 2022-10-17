package com.mokaform.mokaformserver.answer.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mokaform.mokaformserver.answer.domain.EssayAnswer;
import com.mokaform.mokaformserver.answer.domain.MultipleChoiceAnswer;
import com.mokaform.mokaformserver.answer.domain.OXAnswer;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
public class AnswerDetailResponse extends AnswerResponse {

    private final List<EssayAnswerResponse> essayAnswers;

    private final List<MultipleChoiceAnswerResponse> multipleChoiceAnswers;

    private final List<OXAnswerResponse> oxAnswers;

    @Builder
    public AnswerDetailResponse(Long surveyId, Long surveyeeId,
                                List<EssayAnswer> essayAnswers,
                                List<MultipleChoiceAnswer> multipleChoiceAnswers,
                                List<OXAnswer> oxAnswers) {
        super(surveyId, surveyeeId);
        this.essayAnswers = essayAnswers
                .stream()
                .map(EssayAnswerResponse::new).
                collect(Collectors.toList());
        this.multipleChoiceAnswers = multipleChoiceAnswers
                .stream()
                .map(MultipleChoiceAnswerResponse::new)
                .collect(Collectors.toList());
        this.oxAnswers = oxAnswers
                .stream()
                .map(OXAnswerResponse::new)
                .collect(Collectors.toList());
    }
}
