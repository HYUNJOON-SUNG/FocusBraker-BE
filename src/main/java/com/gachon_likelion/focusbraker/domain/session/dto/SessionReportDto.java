package com.gachon_likelion.focusbraker.domain.session.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gachon_likelion.focusbraker.domain.session.entity.SessionReport;
import com.gachon_likelion.focusbraker.global.enums.DistractionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SessionReportDto {

    @JsonProperty("total_duration_seconds")
    @Schema(description = "총 공부 시간 (초)", example = "1800")
    private Integer totalDurationSeconds;

    @JsonProperty("total_reaction_count")
    @Schema(description = "총 반응 횟수", example = "3")
    private Integer totalReactionCount;

    @JsonProperty("hair_reaction_count")
    @Schema(description = "머리카락 반응 수", example = "1")
    private Integer hairReactionCount;

    @JsonProperty("dust_reaction_count")
    @Schema(description = "먼지 반응 수", example = "0")
    private Integer dustReactionCount;

    @JsonProperty("bug_reaction_count")
    @Schema(description = "벌레 반응 수", example = "1")
    private Integer bugReactionCount;

    @JsonProperty("fake_noti_reaction_count")
    @Schema(description = "가짜 알림 반응 수", example = "1")
    private Integer fakeNotiReactionCount;

    @JsonProperty("avg_reaction_time_ms")
    @Schema(description = "평균 반응 시간 (ms, null 가능)", example = "1528")
    private Integer avgReactionTimeMs;

    @JsonProperty("most_reacted_type")
    @Schema(description = "최다 반응 방해 종류 (null 가능)", example = "HAIR")
    private DistractionType mostReactedType;

    @Builder
    public SessionReportDto(Integer totalDurationSeconds, Integer totalReactionCount, Integer hairReactionCount, Integer dustReactionCount, Integer bugReactionCount, Integer fakeNotiReactionCount, Integer avgReactionTimeMs, DistractionType mostReactedType) {
        this.totalDurationSeconds = totalDurationSeconds;
        this.totalReactionCount = totalReactionCount;
        this.hairReactionCount = hairReactionCount;
        this.dustReactionCount = dustReactionCount;
        this.bugReactionCount = bugReactionCount;
        this.fakeNotiReactionCount = fakeNotiReactionCount;
        this.avgReactionTimeMs = avgReactionTimeMs;
        this.mostReactedType = mostReactedType;
    }

    public static SessionReportDto from(SessionReport report) {
        return SessionReportDto.builder()
                .totalDurationSeconds(report.getTotalDurationSeconds())
                .totalReactionCount(report.getTotalReactionCount())
                .hairReactionCount(report.getHairReactionCount())
                .dustReactionCount(report.getDustReactionCount())
                .bugReactionCount(report.getBugReactionCount())
                .fakeNotiReactionCount(report.getFakeNotiReactionCount())
                .avgReactionTimeMs(report.getAvgReactionTimeMs())
                .mostReactedType(report.getMostReactedType())
                .build();
    }
}
