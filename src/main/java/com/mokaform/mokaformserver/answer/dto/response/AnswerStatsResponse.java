package com.mokaform.mokaformserver.answer.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
public class AnswerStatsResponse {

    private final Map<String, List<String>> essayStats;
    private final Map<String, Map<String, Long>> multipleChoiceStats;
    private final Map<String, Map<String, Long>> oxStats;

    @Builder
    public AnswerStatsResponse(Map<String, List<String>> essayStats,
                               Map<String, Map<String, Long>> multipleChoiceStats,
                               Map<String, Map<String, Long>> oxStats) {
        this.essayStats = essayStats;
        this.multipleChoiceStats = multipleChoiceStats;
        this.oxStats = oxStats;
    }
}
