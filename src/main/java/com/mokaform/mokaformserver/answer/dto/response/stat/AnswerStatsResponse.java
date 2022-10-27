package com.mokaform.mokaformserver.answer.dto.response.stat;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
public class AnswerStatsResponse {

    private final List<EssayStat> essayStats;
    private final List<MultipleChoiceStat> multipleChoiceStats;
    private final List<OXStat> oxStats;

    @Builder
    public AnswerStatsResponse(List<EssayStat> essayStats,
                               List<MultipleChoiceStat> multipleChoiceStats,
                               List<OXStat> oxStats) {
        this.essayStats = essayStats;
        this.multipleChoiceStats = multipleChoiceStats;
        this.oxStats = oxStats;
    }
}
