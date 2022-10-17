package com.mokaform.mokaformserver.survey.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
public class AnswerStatsResponse {

    private final Map<Long, List<String>> essayStats;
    private final Map<Long, Map<Long, Long>> multipleChoiceStats;
    private final Map<Long, Map<String, Long>> oxStats;

    @Builder
    public AnswerStatsResponse(Map<Long, List<String>> essayStats,
                               Map<Long, Map<Long, Long>> multipleChoiceStats,
                               Map<Long, Map<String, Long>> oxStats) {
        this.essayStats = essayStats;
        this.multipleChoiceStats = multipleChoiceStats;
        this.oxStats = oxStats;
    }
}
